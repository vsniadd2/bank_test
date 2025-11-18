package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardDto;
import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.dto.card.CardCvvUpdateResponseDto;
import com.example.bankcards.dto.user.AdminUserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final UserMapper userMapper;
    private final CardService cardService;

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getAllCard(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Card> cardPage = cardRepository.findAll(pageable);

        return cardPage.map(cardMapper::toDto);
    }

    @Transactional
    public CardResponseDto approveBlockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (card.getStatus() != CardStatus.BLOCK_REQUESTED) {
            throw new IllegalStateException(
                    "The card does not have a 'block requested' status. Current status:" + card.getStatus()
            );
        }

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);

        return cardMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDto> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page, 100);
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(userMapper::toAdminDto);
    }

    @Transactional(readOnly = true)
    public AdminUserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toAdminDto(user);
    }

    @Transactional
    public AdminUserDto blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isActive())
            throw new IllegalStateException("User is already blocked");

        user.setActive(false);
        userRepository.save(user);

        return userMapper.toAdminDto(user);
    }

    @Transactional
    public AdminUserDto unblockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.isActive())
            throw new IllegalStateException("User is not blocked");

        user.setActive(true);
        userRepository.save(user);

        return userMapper.toAdminDto(user);
    }

    @Transactional
    public CardDto createCard(Long userId) {
        return cardService.createCardForUser(userId);
    }

    @Transactional
    public CardCvvUpdateResponseDto updateCardCvv(Long userId, Long cardId) {
        return cardService.updateCardCvv(userId, cardId);
    }
}
