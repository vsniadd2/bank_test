package com.example.bankcards.dto.card;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CardCvvUpdateResponseDto(
        Long cardId,
        String cardMask,
        String newCvv,
        LocalDateTime updatedAt
) {
}

