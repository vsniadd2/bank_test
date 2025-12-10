package com.example.bankcards.service;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.TokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.CustomUserDetailsService;
import com.example.bankcards.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void register_ok() {
        RegistrationRequest request = new RegistrationRequest(
                "testuser",
                "test@gmail.com",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .build();

        UserDto userDto = new UserDto(1L, "testuser", "test@gmail.com");

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(customUserDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access_token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh_token");
        when(userMapper.toDto(user)).thenReturn(userDto);

        AuthenticationResponse result = authenticationService.register(request);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.success());
        Assertions.assertEquals("access_token", result.accessToken());
    }

    @Test
    void register_emailExists() {
        RegistrationRequest request = new RegistrationRequest(
                "testuser",
                "test@gmail.com",
                "password123"
        );

        when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            authenticationService.register(request);
        });
    }

    @Test
    void authenticate_ok() {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@gmail.com",
                "password123"
        );

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@gmail.com")
                .build();

        UserDto userDto = new UserDto(1L, "testuser", "test@gmail.com");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(customUserDetailsService.loadUserByUsername("test@gmail.com")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("access_token");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refresh_token");
        when(tokenRepository.findAllValidTokensByUser(anyInt())).thenReturn(java.util.Collections.emptyList());
        when(userMapper.toDto(user)).thenReturn(userDto);

        AuthenticationResponse result = authenticationService.authenticate(request);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.success());
        Assertions.assertEquals("access_token", result.accessToken());
    }
}
