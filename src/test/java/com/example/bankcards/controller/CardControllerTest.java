package com.example.bankcards.controller;

import com.example.bankcards.dto.card.BalanceResponseDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {
    @Mock
    private CardService cardService;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CardController cardController;

    @Test
    void createRandomCard_ok() {
        CardDto cardDto = new CardDto("1234567890123456", "12/25", "123");
        when(cardService.generateRandomCard(userDetails)).thenReturn(cardDto);

        ResponseEntity<CardDto> result = cardController.createRandomCard(userDetails);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("1234567890123456", result.getBody().cardNumber());
    }

    @Test
    void getDeposit_ok() {
        Long cardId = 1L;
        BalanceResponseDto balanceDto = BalanceResponseDto.builder()
                .cardId(cardId)
                .cardMask("**** **** **** 1234")
                .balance(new BigDecimal("1000"))
                .build();

        when(cardService.getCardBalance(cardId, userDetails)).thenReturn(balanceDto);

        ResponseEntity<BalanceResponseDto> result = cardController.getDeposit(userDetails, cardId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(new BigDecimal("1000"), result.getBody().balance());
    }
}
