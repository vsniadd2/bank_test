package com.example.bankcards.service;

import com.example.bankcards.dto.card.BalanceResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptionUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardEncryptionUtil cardEncryptionUtil;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private CardService cardService;

    @Test
    void getCardBalance_ok() {
        User user = User.builder().id(1L).email("test@gmail.com").build();
        Card card = Card.builder()
                .id(1L)
                .lastFourDigits("1234")
                .balance(new BigDecimal("777"))
                .user(user)
                .build();

        when(userDetails.getUsername()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(cardRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(card));

        BalanceResponseDto result = cardService.getCardBalance(1L, userDetails);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(new BigDecimal("777"), result.balance());
    }

    @Test
    void getCardBalance_cardNotFound() {
        User user = User.builder().id(1L).email("test@gmail.com").build();

        when(userDetails.getUsername()).thenReturn("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(cardRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            cardService.getCardBalance(1L, userDetails);
        });
    }
}
