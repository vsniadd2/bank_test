package com.example.bankcards.dto.card;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponseDto(
        Long id,
        String lastFourDigits,
        String maskedNumber,
        LocalDate expirationDate,
        CardStatus status,
        BigDecimal balance
) {
}

