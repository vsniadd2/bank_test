package com.example.bankcards.service;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.Token;
import com.example.bankcards.entity.TokenType;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.TokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final TokenRepository tokenRepository;

    /**
     * Регистрирует нового пользователя
     *
     * @param request данные для регистрации
     * @return ответ с токенами аутентификации
     */
    @Transactional
    public AuthenticationResponse register(RegistrationRequest request) {
        validateRegister(request);
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        var savedUser = userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.email());
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        savedUserToken(savedUser, jwtToken);

        UserDto userDto = userMapper.toDto(savedUser);

        return AuthenticationResponse.registration(
                jwtToken,
                refreshToken,
                userDto
        );
    }

    /**
     * Асинхронно регистрирует нового пользователя
     *
     * @param request данные для регистрации
     * @return CompletableFuture с ответом аутентификации
     */
    @Async
    public CompletableFuture<AuthenticationResponse> registerAsync(RegistrationRequest request) {
        return CompletableFuture.supplyAsync(() -> register(request));
    }

    /**
     * Аутентифицирует пользователя
     *
     * @param authenticationRequest данные для аутентификации
     * @return ответ с токенами аутентификации
     */
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.email(), authenticationRequest.password())
        );

        User user = userRepository.findByEmail(authenticationRequest.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.email());
        var jwtToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);
        revokeAllUserToken(user);
        savedUserToken(user, jwtToken);
        savedUserRefreshToken(user, refreshToken);

        UserDto userDto = userMapper.toDto(user);
        return AuthenticationResponse.login(
                jwtToken,
                refreshToken,
                userDto
        );
    }

    /**
     * Асинхронно аутентифицирует пользователя
     *
     * @param authenticationRequest данные для аутентификации
     * @return CompletableFuture с ответом аутентификации
     */
    @Async
    public CompletableFuture<AuthenticationResponse> authenticateAsync(AuthenticationRequest authenticationRequest) {
        return CompletableFuture.supplyAsync(() -> authenticate(authenticationRequest));
    }


    /**
     * Обновляет токен доступа
     *
     * @param authHeader заголовок с токеном авторизации
     * @return ответ с новыми токенами аутентификации
     */
    @Transactional
    public AuthenticationResponse refreshToken(String authHeader) {
        validateRefreshToken(authHeader);
        log.info("sayonara boy");
        String refreshToken = authHeader.substring(7);
        String email = jwtService.getUsername(refreshToken);

        if (email == null)
            throw new IllegalArgumentException("Invalid refresh token");

        String tokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("typ", String.class));
        if (!"refresh_token".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid token type for refresh");
        }

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken, userDetails))
            throw new IllegalArgumentException("Refresh token is not valid");

        var storedRefreshToken = tokenRepository.findByToken(refreshToken);
        if (storedRefreshToken.isEmpty() || storedRefreshToken.get().isExpired() || storedRefreshToken.get().isRevoked()) {
            throw new IllegalArgumentException("Refresh token is not valid or has been revoked");
        }

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        revokeAllUserToken(user);
        savedUserToken(user, newAccessToken);
        savedUserRefreshToken(user, newRefreshToken);

        UserDto userDto = userMapper.toDto(user);
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .success(true)
                .user(userDto)
                .build();
    }

    /**
     * Асинхронно обновляет токен доступа
     *
     * @param authHeader заголовок с токеном авторизации
     * @return CompletableFuture с ответом аутентификации
     */
    public CompletableFuture<AuthenticationResponse> refreshTokenAsync(String authHeader) {
        return CompletableFuture.supplyAsync(() -> refreshToken(authHeader));
    }

    private void revokeAllUserToken(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId().intValue());
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void savedUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.ACCESS)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void savedUserRefreshToken(User user, String refreshToken) {
        var token = Token.builder()
                .user(user)
                .token(refreshToken)
                .tokenType(TokenType.REFRESH)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void validateRegister(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    private void validateRefreshToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new IllegalArgumentException("Missing or invalid Authorization header");
    }


}
