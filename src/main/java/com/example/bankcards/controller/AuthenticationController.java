package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Регистрирует нового пользователя
     *
     * @param request данные для регистрации
     * @return ответ с токенами аутентификации
     */
    @PostMapping("/registration")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> registration(@Valid @RequestBody RegistrationRequest request) {
        return authenticationService.registerAsync(request)
                .thenApply(response ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(response)
                );
    }

    /**
     * Аутентифицирует пользователя
     *
     * @param authenticationRequest данные для аутентификации
     * @return ответ с токенами аутентификации
     */
    @PostMapping("/authenticate")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticateAsync(authenticationRequest)
                .thenApply(response ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(response)
                );
    }

    /**
     * Обновляет токен доступа
     *
     * @param authHeader заголовок с токеном авторизации
     * @return ответ с новыми токенами аутентификации
     */
    @PostMapping("/refresh-token")
    public CompletableFuture<ResponseEntity<AuthenticationResponse>> refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) {
        return authenticationService.refreshTokenAsync(authHeader)
                .thenApply(response ->
                        ResponseEntity
                                .status(HttpStatus.OK)
                                .body(response)
                );
    }
}