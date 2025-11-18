package com.example.bankcards.service;

import com.example.bankcards.dto.card.BalanceResponseDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.dto.card.CardCvvUpdateResponseDto;
import com.example.bankcards.dto.transaction.DepositRequestDto;
import com.example.bankcards.dto.transaction.DepositResponseDto;
import com.example.bankcards.dto.transaction.MoneyTransactionRequestDto;
import com.example.bankcards.dto.transaction.MoneyTransactionResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardEncryptionUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardEncryptionUtil cardEncryptionUtil;
    private final CardMapper cardMapper;
    private final Random random = new Random();

    public CardDto generateRandomCard(UserDetails userDetails) {
        String cardNumber = generateCardNumber();
        String expiryDateStr = generateExpiryDate();
        String cvv = String.format("%03d", random.nextInt(1000));

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String encryptedNumber = cardEncryptionUtil.encryptCardNumber(cardNumber);
        String lastFourDigits = cardEncryptionUtil.extractLastFourDigits(cardNumber);

        LocalDate expirationDate = parseExpiryDate(expiryDateStr);

        Card card = Card.builder()
                .encryptedNumber(encryptedNumber)
                .lastFourDigits(lastFourDigits)
                .user(user)
                .encryptedCvv(cardEncryptionUtil.encryptValue(cvv))
                .expirationDate(expirationDate)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        cardRepository.save(card);

        return new CardDto(cardNumber, expiryDateStr, cvv);
    }

    @Transactional
    public CardDto createCardForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        String cardNumber = generateCardNumber();
        String expiryDateStr = generateExpiryDate();
        String cvv = String.format("%03d", random.nextInt(1000));

        String encryptedNumber = cardEncryptionUtil.encryptCardNumber(cardNumber);
        String lastFourDigits = cardEncryptionUtil.extractLastFourDigits(cardNumber);
        LocalDate expirationDate = parseExpiryDate(expiryDateStr);

        Card card = Card.builder()
                .encryptedNumber(encryptedNumber)
                .lastFourDigits(lastFourDigits)
                .user(user)
                .encryptedCvv(cardEncryptionUtil.encryptValue(cvv))
                .expirationDate(expirationDate)
                .status(CardStatus.ACTIVE)
                .balance(BigDecimal.ZERO)
                .build();

        cardRepository.save(card);

        return new CardDto(cardNumber, expiryDateStr, cvv);
    }

    @Transactional
    public CardCvvUpdateResponseDto updateCardCvv(Long userId, Long cardId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Card not found or does not belong to the specified user"
                ));

        String newCvv = String.format("%03d", random.nextInt(1000));
        card.setEncryptedCvv(cardEncryptionUtil.encryptValue(newCvv));
        cardRepository.save(card);

        return CardCvvUpdateResponseDto.builder()
                .cardId(card.getId())
                .cardMask(card.getMaskedNumber())
                .newCvv(newCvv)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCardsByUserId(@NonNull UserDetails userDetails, int page) {
        Pageable pageable = PageRequest.of(page, 2);

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));

        Page<Card> cardsPage = cardRepository.findAllByUserId(user.getId(), pageable);

        return cardsPage.map(cardMapper::toDto);
    }

    @Transactional
    public MoneyTransactionResponseDto transactionBetweenCards(
            MoneyTransactionRequestDto request,
            @NonNull UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (request.fromCardId().equals(request.toCardId()))
            throw new IllegalArgumentException("You cannot transfer from one card to another");


        Card fromCard = cardRepository.findByIdAndUserId(request.fromCardId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "The sender's card was not found or does not belong to you."
                ));

        Card toCard = cardRepository.findByIdAndUserId(request.toCardId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "The recipient's card was not found or does not belong to you."
                ));

        if (fromCard.getStatus() != CardStatus.ACTIVE)
            throw new IllegalArgumentException("The sender's card is inactive. Status: " + fromCard.getStatus().toString());

        if (toCard.getStatus() != CardStatus.ACTIVE)
            throw new IllegalArgumentException("The recipient's card is inactive. Status: " + toCard.getStatus().toString());

        if (fromCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("The sender's card has expired.");
        }

        if (toCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("The recipient's card has expired.");
        }

        if (fromCard.getBalance().compareTo(request.amount()) < 0)
            throw new IllegalArgumentException(String.format("Insufficient funds. Available %s. Required %s.",
                    fromCard.getBalance().toString(),
                    request.amount()
            ));

        String message = (request.message() != null && !request.message().isEmpty())
                ? request.message()
                : "";

        fromCard.setBalance(fromCard.getBalance().subtract(request.amount()));
        toCard.setBalance(toCard.getBalance().add(request.amount()));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
        return MoneyTransactionResponseDto.builder()
                .success(true)
                .message(message)
                .fromCardId(fromCard.getId())
                .fromCardMask(fromCard.getMaskedNumber())
                .toCardId(toCard.getId())
                .toCardMask(toCard.getMaskedNumber())
                .amount(request.amount())
                .fromCardBalance(fromCard.getBalance())
                .toCardBalance(toCard.getBalance())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Transactional
    public DepositResponseDto depositToCard(
            DepositRequestDto request,
            @NonNull UserDetails userDetails
    ) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Card card = cardRepository.findByIdAndUserId(request.cardId(), user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Карта не найдена или не принадлежит вам"
                ));

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Карта неактивна. Статус: " + card.getStatus()
            );
        }

        if (card.getExpirationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Срок действия карты истек");
        }

        card.setBalance(card.getBalance().add(request.amount()));

        cardRepository.save(card);

        return DepositResponseDto.builder()
                .success(true)
                .message("Replenishment")
                .cardId(card.getId())
                .cardMask(card.getMaskedNumber())
                .amount(request.amount())
                .newBalance(card.getBalance())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Transactional(readOnly = true)
    public BalanceResponseDto getCardBalance(Long cardId, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("The card was not found or does not belong to you."));

        return BalanceResponseDto.builder()
                .cardId(card.getId())
                .cardMask(card.getMaskedNumber())
                .balance(card.getBalance())
                .build();
    }

    @Transactional
    public CardResponseDto blockCard(Long cardId, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Card card = cardRepository.findByIdAndUserId(cardId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("The card was not found or does not belong to you."));

        if (card.getStatus() == CardStatus.BLOCKED)
            throw new IllegalStateException("The card is already blocked");

        if (card.getStatus() == CardStatus.BLOCK_REQUESTED)
            throw new IllegalStateException("The block request has already been sent. Administrator approval is pending.");

        if (card.getStatus() == CardStatus.EXPIRED)
            throw new IllegalStateException("You can't block an expired card.");

        card.setStatus(CardStatus.BLOCK_REQUESTED);
        cardRepository.save(card);

        return cardMapper.toDto(card);
    }


    private LocalDate parseExpiryDate(String expiryDate) {
        String[] parts = expiryDate.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        return LocalDate.of(year, month, 1).withDayOfMonth(
                LocalDate.of(year, month, 1).lengthOfMonth()
        );
    }

    private String generateCardNumber() {
        String bin = "400000";
        long randomPart = Math.abs(random.nextLong() % 1_000_000_0000L);
        return bin + String.format("%010d", randomPart);
    }


    private String generateExpiryDate() {
        int month = random.nextInt(12) + 1;
        int year = 2025 + random.nextInt(5);
        return String.format("%02d/%d", month, year);
    }
}
