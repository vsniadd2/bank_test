package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.dto.card.CardCvvUpdateResponseDto;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * Получает все карты с пагинацией
     *
     * @param page номер страницы (по умолчанию 0)
     * @return страница с картами
     */
    @Operation(summary = "Получить все карты", description = "Возвращает список всех карт с пагинацией")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка карт")
    @GetMapping("/all-cards")
    public ResponseEntity<Page<CardResponseDto>> getAllCards(@RequestParam(defaultValue = "0") int page) {
        Page<CardResponseDto> allCard = adminService.getAllCard(page);
        return ResponseEntity.ok(allCard);
    }

    /**
     * Одобряет блокировку карты
     *
     * @param cardId идентификатор карты
     * @return информация о карте после одобрения блокировки
     */
    @Operation(summary = "Одобрить блокировку карты", description = "Администратор подтверждает блокировку карты")
    @ApiResponse(responseCode = "200", description = "Карта успешно заблокирована")
    @PostMapping("/cards/{cardId}/approve-block")
    public ResponseEntity<CardResponseDto> approveBlockCard(@PathVariable Long cardId) {
        CardResponseDto response = adminService.approveBlockCard(cardId);
        return ResponseEntity.ok(response);
    }

    /**
     * Получает всех пользователей с пагинацией
     *
     * @param page номер страницы (по умолчанию 0)
     * @return страница с пользователями
     */
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список пользователей с пагинацией")
    @ApiResponse(responseCode = "200",description = "Пользователи получены")
    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserDto>> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        Page<AdminUserDto> users = adminService.getAllUsers(page);
        return ResponseEntity.ok(users);
    }

    /**
     * Получает пользователя по идентификатору
     *
     * @param userId идентификатор пользователя
     * @return информация о пользователе
     */
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе по идентификатору")
    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable Long userId) {
        AdminUserDto user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Блокирует пользователя
     *
     * @param userId идентификатор пользователя
     * @return информация о заблокированном пользователе
     */
    @Operation(summary = "Блокировать пользователя", description = "Администратор блокирует пользователя по ID")
    @PatchMapping("/users/{userId}/block")
    public ResponseEntity<AdminUserDto> blockUser(@PathVariable Long userId) {
        AdminUserDto user = adminService.blockUser(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Разблокирует пользователя
     *
     * @param userId идентификатор пользователя
     * @return информация о разблокированном пользователе
     */
    @Operation(summary = "Разблокировать пользователя", description = "Администратор снимает блокировку с пользователя")
    @PatchMapping("/users/{userId}/unblock")
    public ResponseEntity<AdminUserDto> unblockUser(@PathVariable Long userId) {
        AdminUserDto user = adminService.unblockUser(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * Создает новую карту для пользователя
     *
     * @param userId идентификатор пользователя
     * @return информация о созданной карте
     */
    @Operation(summary = "Создать карту", description = "Создаёт новую карту для пользователя")
    @PostMapping("/users/{userId}/cards")
    public ResponseEntity<CardDto> createCard(@PathVariable Long userId) {
        CardDto card = adminService.createCard(userId);
        return ResponseEntity.ok(card);
    }

    /**
     * Обновляет CVV код карты
     *
     * @param userId идентификатор пользователя
     * @param cardId идентификатор карты
     * @return информация об обновленном CVV коде
     */
    @Operation(summary = "Обновить CVV карты", description = "Обновляет CVV код карты пользователя")
    @PatchMapping("/users/{userId}/cards/{cardId}/cvv")
    public ResponseEntity<CardCvvUpdateResponseDto> updateCardCvv(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CardCvvUpdateResponseDto response = adminService.updateCardCvv(userId, cardId);
        return ResponseEntity.ok(response);
    }

}
