package com.example.bankcards.dto.transaction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа пополнения карты
 *
 * @param success флаг успешности операции
 * @param message сообщение
 * @param cardId идентификатор карты
 * @param cardMask замаскированный номер карты
 * @param amount сумма пополнения
 * @param newBalance новый баланс карты
 * @param timestamp время выполнения операции
 */
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

