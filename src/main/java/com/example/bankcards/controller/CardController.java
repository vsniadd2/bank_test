package com.example.bankcards.controller;

import com.example.bankcards.dto.card.BalanceResponseDto;
import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.dto.transaction.DepositRequestDto;
import com.example.bankcards.dto.transaction.DepositResponseDto;
import com.example.bankcards.dto.transaction.MoneyTransactionRequestDto;
import com.example.bankcards.dto.transaction.MoneyTransactionResponseDto;
import com.example.bankcards.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/card")
public class CardController {
    private final CardService cardService;


    /**
     * Создает случайную карту для текущего пользователя
     *
     * @param userDetails данные текущего пользователя
     * @return информация о созданной карте
     */
    @Operation(summary = "Создать случайную карту", description = "Генерирует новую случайную карту для текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Карта успешно создана")
    @PostMapping
    public ResponseEntity<CardDto> createRandomCard(@AuthenticationPrincipal UserDetails userDetails) {
        CardDto dto = cardService.generateRandomCard(userDetails);
        return ResponseEntity.ok(dto);
    }

    /**
     * Получает все карты текущего пользователя с пагинацией
     *
     * @param userDetails данные текущего пользователя
     * @param page номер страницы (по умолчанию 0)
     * @return страница с картами пользователя
     */
    @Operation(summary = "Получить карты пользователя", description = "Возвращает все карты текущего пользователя с пагинацией")
    @ApiResponse(responseCode = "200", description = "Карты успешно получены")
    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getUserCards(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page
    ) {
        var cards = cardService.getAllCardsByUserId(userDetails, page);
        return ResponseEntity.ok(cards);
    }

    /**
     * Выполняет перевод между картами
     *
     * @param userDetails данные текущего пользователя
     * @param transferDto данные для перевода
     * @return информация о выполненной транзакции
     */
    @Operation(summary = "Перевод между картами", description = "Выполняет перевод средств между картами пользователя")
    @ApiResponse(responseCode = "200", description = "Перевод выполнен успешно")
    @PostMapping("/transfer")
    public ResponseEntity<MoneyTransactionResponseDto> transfer(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MoneyTransactionRequestDto transferDto
    ) {
        var response = cardService.transactionBetweenCards(transferDto, userDetails);
        return ResponseEntity.ok(response);
    }

    /**
     * Пополняет баланс карты
     *
     * @param userDetails данные текущего пользователя
     * @param request данные для пополнения
     * @return информация о пополнении баланса
     */
    @Operation(summary = "Пополнение карты", description = "Пополняет баланс выбранной карты")
    @ApiResponse(responseCode = "200", description = "Баланс успешно пополнен")
    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> deposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody DepositRequestDto request
    ) {
        DepositResponseDto response = cardService.depositToCard(request, userDetails);
        return ResponseEntity.ok(response);
    }

    /**
     * Получает баланс карты
     *
     * @param userDetails данные текущего пользователя
     * @param cardId идентификатор карты
     * @return информация о балансе карты
     */
    @Operation(summary = "Получить баланс карты", description = "Возвращает текущий баланс указанной карты")
    @ApiResponse(responseCode = "200", description = "Баланс успешно получен")
    @GetMapping("/deposit")
    public ResponseEntity<BalanceResponseDto> getDeposit(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Long cardId
    ) {
        var cardBalance = cardService.getCardBalance(cardId, userDetails);
        return ResponseEntity.ok(cardBalance);
    }

    /**
     * Блокирует карту
     *
     * @param userDetails данные текущего пользователя
     * @param cardId идентификатор карты
     * @return информация о заблокированной карте
     */
    @Operation(summary = "Блокировка карты", description = "Блокирует указанную карту текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Карта успешно заблокирована")
    @PostMapping("/block-card")
    public ResponseEntity<CardResponseDto> blockCard(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Long cardId
    ) {
        var cardResponseDto = cardService.blockCard(cardId, userDetails);
        return ResponseEntity.ok(cardResponseDto);
    }
}
