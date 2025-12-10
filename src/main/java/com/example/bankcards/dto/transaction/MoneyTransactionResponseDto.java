package com.example.bankcards.dto.transaction;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для ответа перевода между картами
 *
 * @param success флаг успешности операции
 * @param message сообщение
 * @param fromCardId идентификатор карты отправителя
 * @param fromCardMask замаскированный номер карты отправителя
 * @param toCardId идентификатор карты получателя
 * @param toCardMask замаскированный номер карты получателя
 * @param amount сумма перевода
 * @param fromCardBalance баланс карты отправителя после перевода
 * @param toCardBalance баланс карты получателя после перевода
 * @param timestamp время выполнения операции
 */
@Builder
public record MoneyTransactionResponseDto(
        boolean success,
        String message,
        Long fromCardId,
        String fromCardMask,
        Long toCardId,
        String toCardMask,
        BigDecimal amount,
        BigDecimal fromCardBalance,
        BigDecimal toCardBalance,
        LocalDateTime timestamp
) {
}
