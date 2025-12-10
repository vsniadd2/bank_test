package com.example.bankcards.dto.card;

import lombok.Builder;

import java.math.BigDecimal;

/**
 * DTO для ответа с балансом карты
 *
 * @param cardId идентификатор карты
 * @param cardMask замаскированный номер карты
 * @param balance баланс карты
 */
@Builder
public record BalanceResponseDto(
        Long cardId,
        String cardMask,
        BigDecimal balance
) {
}
