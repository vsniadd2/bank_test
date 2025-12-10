package com.example.bankcards.dto.transaction;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * DTO для запроса перевода между картами
 *
 * @param fromCardId идентификатор карты отправителя
 * @param toCardId идентификатор карты получателя
 * @param message сообщение к переводу (опционально)
 * @param amount сумма перевода
 */
public record MoneyTransactionRequestDto(
        @NotNull(message = "Required field")
        Long fromCardId,

        @NotNull(message = "Required field")
        Long toCardId,

        @Nullable
        String message,

        @NotNull(message = "Required field")
        @Positive(message = "The amount must be greater than 0")
        @DecimalMin(value = "0.01", message = "Minimum transfer amount 0.01")
        BigDecimal amount
) {
}
