package com.sparta.springusersetting.domain.lists.dto.response;

import com.sparta.springusersetting.domain.card.dto.CardResponseDto;
import com.sparta.springusersetting.domain.card.dto.GetListsCardResponseDto;
import com.sparta.springusersetting.domain.card.dto.ListsCardResponseDto;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetListsResponseDto {
    private Long id;
    private String title;
    private Integer pos;
    private List<GetListsCardResponseDto> cardList;

    public GetListsResponseDto(Lists lists){
        this.id = lists.getId();
        this.title = lists.getTitle();
        this.pos = lists.getPos();
        this.cardList = lists.getCardList().stream()
                .map(GetListsCardResponseDto::new)
                .collect(Collectors.toList());
    }
}
