package com.example.bankcards.dto.auth;

import com.example.bankcards.dto.user.UserDto;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO для ответа аутентификации
 *
 * @param accessToken токен доступа
 * @param refreshToken токен обновления
 * @param success флаг успешности операции
 * @param message сообщение
 * @param user информация о пользователе
 * @param timestamp время выполнения операции
 */
@Builder
public record AuthenticationResponse(
        String accessToken,
        String refreshToken,
        boolean success,
        String message,
        UserDto user,
        LocalDateTime timestamp
) {
    /**
     * Создает ответ для регистрации
     *
     * @param accessToken токен доступа
     * @param refreshToken токен обновления
     * @param userDto информация о пользователе
     * @return ответ аутентификации
     */
    public static AuthenticationResponse registration(String accessToken, String refreshToken, UserDto userDto) {
        String message = "Registration successful";
        return new AuthenticationResponse(
                accessToken,
                refreshToken,
                true,
                message,
                userDto,
                LocalDateTime.now()
        );
    }

    /**
     * Создает ответ для входа
     *
     * @param accessToken токен доступа
     * @param refreshToken токен обновления
     * @param userDto информация о пользователе
     * @return ответ аутентификации
     */
    public static AuthenticationResponse login(String accessToken, String refreshToken, UserDto userDto) {
        String message = "Login successful";
        return new AuthenticationResponse(
                accessToken,
                refreshToken,
                true,
                message,
                userDto,
                LocalDateTime.now()
        );
    }

}
