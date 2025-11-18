package com.example.bankcards.controller;

import com.example.bankcards.dto.card.BalanceResponseDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.transaction.DepositRequestDto;
import com.example.bankcards.dto.transaction.DepositResponseDto;
import com.example.bankcards.dto.transaction.MoneyTransactionRequestDto;
import com.example.bankcards.dto.transaction.MoneyTransactionResponseDto;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {
    private final CardService cardService;


    @PostMapping
    public ResponseEntity<?> createRandomCard(@AuthenticationPrincipal UserDetails userDetails) {
        CardDto dto = cardService.generateRandomCard(userDetails);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<?> getUserCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page
    ) {
        var cards = cardService.getAllCardsByUserId(userDetails, page);
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/transfer")
    public ResponseEntity<MoneyTransactionResponseDto> transfer(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MoneyTransactionRequestDto transferDto
    ) {
        var response = cardService.transactionBetweenCards(transferDto, userDetails);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DepositRequestDto request
    ) {
        DepositResponseDto response = cardService.depositToCard(request, userDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deposit")
    public ResponseEntity<BalanceResponseDto> getDeposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Long cardId
    ) {
        var cardBalance = cardService.getCardBalance(cardId, userDetails);
        return ResponseEntity.ok(cardBalance);
    }

    @PostMapping("/block-card")
    public ResponseEntity<?> blockCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Long cardId
    ) {
        var cardResponseDto = cardService.blockCard(cardId, userDetails);
        return ResponseEntity.ok(cardResponseDto);
    }
}
