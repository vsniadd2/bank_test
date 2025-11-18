package com.example.bankcards.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExceptionDto(
        boolean success,
        String message,
        LocalDateTime timestamp,
        int status,
        String errorType
) {
}
