package com.example.bankcards.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для запроса регистрации
 *
 * @param username имя пользователя
 * @param email email пользователя
 * @param password пароль пользователя
 */
public record RegistrationRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 4,message = "Username must be at least 4 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8,message = "Password must be at least 8 characters")
        String password

) {
}
