package com.example.bankcards.dto.card;

import com.example.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для ответа с информацией о карте
 *
 * @param id идентификатор карты
 * @param lastFourDigits последние четыре цифры номера карты
 * @param maskedNumber замаскированный номер карты
 * @param expirationDate срок действия карты
 * @param status статус карты
 * @param balance баланс карты
 */
public record CardResponseDto(
        Long id,
        String lastFourDigits,
        String maskedNumber,
        LocalDate expirationDate,
        CardStatus status,
        BigDecimal balance
) {
}

