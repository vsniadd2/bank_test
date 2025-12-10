package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-10T22:05:21+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.9 (Amazon.com Inc.)"
)
@Component
public class CardMapperImpl implements CardMapper {

    @Override
    public CardResponseDto toDto(Card card) {
        if ( card == null ) {
            return null;
        }

        Long id = null;
        String lastFourDigits = null;
        String maskedNumber = null;
        LocalDate expirationDate = null;
        CardStatus status = null;
        BigDecimal balance = null;

        id = card.getId();
        lastFourDigits = card.getLastFourDigits();
        maskedNumber = card.getMaskedNumber();
        expirationDate = card.getExpirationDate();
        status = card.getStatus();
        balance = card.getBalance();

        CardResponseDto cardResponseDto = new CardResponseDto( id, lastFourDigits, maskedNumber, expirationDate, status, balance );

        return cardResponseDto;
    }
}
