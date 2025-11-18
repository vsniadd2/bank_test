package com.example.bankcards.dto.card;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceResponseDto(
        Long cardId,
        String cardMask,
        BigDecimal balance
) {
}
