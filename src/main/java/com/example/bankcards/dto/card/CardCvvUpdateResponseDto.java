package com.example.bankcards.dto.card;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * DTO для ответа обновления CVV кода
 *
 * @param cardId идентификатор карты
 * @param cardMask замаскированный номер карты
 * @param newCvv новый CVV код
 * @param updatedAt время обновления
 */
@Builder
public record CardCvvUpdateResponseDto(
        Long cardId,
        String cardMask,
        String newCvv,
        LocalDateTime updatedAt
) {
}

