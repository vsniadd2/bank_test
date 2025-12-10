package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса аутентификации
 *
 * @param email email пользователя
 * @param password пароль пользователя
 */
public record AuthenticationRequest(
        @NotBlank
        @Email
        String email,
        @Size(min = 8)
        String password

) {
}
