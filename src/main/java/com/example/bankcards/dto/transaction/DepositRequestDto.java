package com.example.bankcards.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO для запроса пополнения карты
 *
 * @param cardId идентификатор карты
 * @param amount сумма пополнения
 */
public record DepositRequestDto(
        @NotNull(message = "Required field")
        Long cardId,

        @NotNull(message = "Required field")
        @Positive(message = "The amount must be greater than 0")
        @DecimalMin(value = "0.01", message = "Minimum transfer amount 0.01")
        BigDecimal amount
) {
}

