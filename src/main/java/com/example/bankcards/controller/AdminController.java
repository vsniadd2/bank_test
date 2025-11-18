package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.dto.card.CardCvvUpdateResponseDto;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("all-cards")
    public ResponseEntity<?> getAllCards(@RequestParam(defaultValue = "0") int page) {
        Page<CardResponseDto> allCard = adminService.getAllCard(page);
        return ResponseEntity.ok(allCard);
    }

    @PostMapping("/cards/{cardId}/approve-block")
    public ResponseEntity<CardResponseDto> approveBlockCard(@PathVariable Long cardId) {
        CardResponseDto response = adminService.approveBlockCard(cardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserDto>> getAllUsers(@RequestParam(defaultValue = "0") int page) {
        Page<AdminUserDto> users = adminService.getAllUsers(page);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserDto> getUserById(@PathVariable Long userId) {
        AdminUserDto user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}/block")
    public ResponseEntity<AdminUserDto> blockUser(@PathVariable Long userId) {
        AdminUserDto user = adminService.blockUser(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/users/{userId}/unblock")
    public ResponseEntity<AdminUserDto> unblockUser(@PathVariable Long userId) {
        AdminUserDto user = adminService.unblockUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/{userId}/cards")
    public ResponseEntity<CardDto> createCard(@PathVariable Long userId) {
        CardDto card = adminService.createCard(userId);
        return ResponseEntity.ok(card);
    }

    @PatchMapping("/users/{userId}/cards/{cardId}/cvv")
    public ResponseEntity<CardCvvUpdateResponseDto> updateCardCvv(
            @PathVariable Long userId,
            @PathVariable Long cardId
    ) {
        CardCvvUpdateResponseDto response = adminService.updateCardCvv(userId, cardId);
        return ResponseEntity.ok(response);
    }

}
