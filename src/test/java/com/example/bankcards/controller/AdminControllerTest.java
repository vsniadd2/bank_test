package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void getUserById_ok() {
        Long userId = 1L;
        AdminUserDto userDto = new AdminUserDto(
                userId,
                "testuser",
                "test@gmail.com",
                null,
                true,
                null
        );

        when(adminService.getUserById(userId)).thenReturn(userDto);

        ResponseEntity<AdminUserDto> result = adminController.getUserById(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userId, result.getBody().id());
        assertEquals("test@gmail.com", result.getBody().email());
    }

    @Test
    void createCard_ok() {
        Long userId = 1L;
        CardDto cardDto = new CardDto("1234567890123456", "12/25", "123");

        when(adminService.createCard(userId)).thenReturn(cardDto);

        ResponseEntity<CardDto> result = adminController.createCard(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("1234567890123456", result.getBody().cardNumber());
    }
}
