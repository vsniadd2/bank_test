package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.AuthenticationRequest;
import com.example.bankcards.dto.auth.AuthenticationResponse;
import com.example.bankcards.dto.auth.RegistrationRequest;
import com.example.bankcards.dto.user.UserDto;
import com.example.bankcards.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void registration_ok() throws Exception {
        RegistrationRequest request = new RegistrationRequest(
                "testuser",
                "test@gmail.com",
                "password123"
        );

        UserDto userDto = new UserDto(1L, "testuser", "test@gmail.com");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("token")
                .refreshToken("refresh")
                .success(true)
                .user(userDto)
                .build();

        CompletableFuture<AuthenticationResponse> future = CompletableFuture.completedFuture(response);
        when(authenticationService.registerAsync(request)).thenReturn(future);

        CompletableFuture<ResponseEntity<AuthenticationResponse>> result = authenticationController.registration(request);
        ResponseEntity<AuthenticationResponse> responseEntity = result.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().success());
    }

    @Test
    void authenticate_ok() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "test@gmail.com",
                "password123"
        );

        UserDto userDto = new UserDto(1L, "testuser", "test@gmail.com");
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("token")
                .refreshToken("refresh")
                .success(true)
                .user(userDto)
                .build();

        CompletableFuture<AuthenticationResponse> future = CompletableFuture.completedFuture(response);
        when(authenticationService.authenticateAsync(request)).thenReturn(future);

        CompletableFuture<ResponseEntity<AuthenticationResponse>> result = authenticationController.authenticate(request);
        ResponseEntity<AuthenticationResponse> responseEntity = result.get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }
}
