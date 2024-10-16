package com.sparta.springusersetting.domain.card.repository;

import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {
    Page<CardSearchResponseDto> searchCards(CardSearchRequestDto searchRequest, Pageable pageable);
}
