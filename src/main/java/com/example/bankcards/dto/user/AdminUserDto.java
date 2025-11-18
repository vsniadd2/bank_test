package com.example.bankcards.dto.user;

import com.example.bankcards.entity.Role;

import java.time.LocalDateTime;
import java.util.List;

public record AdminUserDto(
        Long id,
        String username,
        String email,
        List<Role> roles,
        boolean isActive,
        LocalDateTime dateTimeOfCreated
) {
}

