package com.sparta.springusersetting.domain.card.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CardTopViewListResponseDto {
    private final List<String> topViewCardList;
}
