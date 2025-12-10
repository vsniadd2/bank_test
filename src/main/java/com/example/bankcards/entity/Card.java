package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сущность банковской карты
 */
@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "user")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "card_number_encrypted", nullable = false, length = 512)
    String encryptedNumber;

    
    @Column(name = "card_number_last_four", nullable = false, length = 4)
    String lastFourDigits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "cvv_encrypted", length = 255)
    @JsonIgnore
    String encryptedCvv;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    CardStatus status;

    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    BigDecimal balance;

    /**
     * Возвращает замаскированный номер карты
     *
     * @return замаскированный номер карты
     */
    @Transient
    public String getMaskedNumber() {
        return "**** **** **** " + lastFourDigits;
    }
}
