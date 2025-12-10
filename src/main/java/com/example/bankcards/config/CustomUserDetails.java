package com.example.bankcards.config;

import com.example.bankcards.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация UserDetails для Spring Security
 */
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

    /**
     * Возвращает роли пользователя
     *
     * @return коллекция ролей пользователя
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает пароль пользователя
     *
     * @return пароль пользователя
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Возвращает email пользователя как username
     *
     * @return email пользователя
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта
     *
     * @return true (срок действия не истекает)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт
     *
     * @return true (аккаунт не заблокирован)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных
     *
     * @return true (срок действия не истекает)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активен ли пользователь
     *
     * @return статус активности пользователя
     */
    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}