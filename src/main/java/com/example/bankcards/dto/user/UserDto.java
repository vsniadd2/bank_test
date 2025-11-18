package com.example.bankcards.dto.user;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String username,
        String email
) {
}
