package com.example.bankcards.dto.user;

import com.example.bankcards.entity.Role;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для информации о пользователе (для администратора)
 *
 * @param id идентификатор пользователя
 * @param username имя пользователя
 * @param email email пользователя
 * @param roles роли пользователя
 * @param isActive флаг активности пользователя
 * @param dateTimeOfCreated дата создания пользователя
 */
public record AdminUserDto(
        Long id,
        String username,
        String email,
        List<Role> roles,
        boolean isActive,
        LocalDateTime dateTimeOfCreated
) {
}

