package com.example.bankcards.dto.user;

import lombok.Builder;

/**
 * DTO для информации о пользователе
 *
 * @param id идентификатор пользователя
 * @param username имя пользователя
 * @param email email пользователя
 */
@Builder
public record UserDto(
        Long id,
        String username,
        String email
) {
}
