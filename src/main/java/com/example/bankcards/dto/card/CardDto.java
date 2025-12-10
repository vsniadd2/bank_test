package com.example.bankcards.dto.card;

/**
 * DTO для информации о карте
 *
 * @param cardNumber номер карты
 * @param expiryDate срок действия карты
 * @param cvv CVV код карты
 */
public record CardDto(
        String cardNumber,
        String expiryDate,
        String cvv
) {
}
