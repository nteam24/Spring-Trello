package com.sparta.springusersetting.domain.card.repository;

import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import org.springframework.data.domain.Slice;

public interface CardRepositoryCustom {
    Slice<CardSearchResponseDto> searchCards(CardSearchRequestDto searchRequest, Long cursorId, int pageSize);
}
