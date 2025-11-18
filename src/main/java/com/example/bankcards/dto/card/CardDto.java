package com.example.bankcards.dto.card;

public record CardDto(
        String cardNumber,
        String expiryDate,
        String cvv
) {
}
