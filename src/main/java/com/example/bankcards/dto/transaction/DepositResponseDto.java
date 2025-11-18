package com.example.bankcards.dto.transaction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record DepositResponseDto(
        boolean success,
        String message,
        Long cardId,
        String cardMask,
        BigDecimal amount,
        BigDecimal newBalance,
        LocalDateTime timestamp
) {
}

