package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Находит пользователя по email
     *
     * @param email email пользователя
     * @return Optional с пользователем, если найден
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
