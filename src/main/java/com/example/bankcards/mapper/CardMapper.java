package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardResponseDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResponseDto toDto(Card card);
}

