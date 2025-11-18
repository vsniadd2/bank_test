package com.example.bankcards.dto.transaction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record MoneyTransactionResponseDto(
        boolean success,
        String message,
        Long fromCardId,
        String fromCardMask,
        Long toCardId,
        String toCardMask,
        BigDecimal amount,
        BigDecimal fromCardBalance,
        BigDecimal toCardBalance,
        LocalDateTime timestamp
) {
}
