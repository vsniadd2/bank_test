package com.example.bankcards.dto.card;

/**
 * DTO для запроса баланса карты
 *
 * @param cardId идентификатор карты
 */
public record BalanceRequestDto(
        Long cardId
) {
}
