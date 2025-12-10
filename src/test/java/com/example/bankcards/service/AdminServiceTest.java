package com.example.bankcards.service;

import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CardService cardService;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getUserById_ok() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .username("testuser")
                .build();

        AdminUserDto adminUserDto = new AdminUserDto(
                userId,
                "testuser",
                "test@gmail.com",
                null,
                true,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toAdminDto(user)).thenReturn(adminUserDto);

        AdminUserDto result = adminService.getUserById(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.id());
        Assertions.assertEquals("test@gmail.com", result.email());
    }

    @Test
    void getUserById_userNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            adminService.getUserById(userId);
        });
    }

    @Test
    void blockUser_ok() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .isActive(true)
                .build();

        AdminUserDto adminUserDto = new AdminUserDto(
                userId,
                "testuser",
                "test@gmail.com",
                null,
                false,
                null
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toAdminDto(user)).thenReturn(adminUserDto);

        AdminUserDto result = adminService.blockUser(userId);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isActive());
    }

    @Test
    void approveBlockCard_ok() {
        Long cardId = 1L;
        Card card = Card.builder()
                .id(cardId)
                .status(CardStatus.BLOCK_REQUESTED)
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        adminService.approveBlockCard(cardId);

        Assertions.assertEquals(CardStatus.BLOCKED, card.getStatus());
    }
}
