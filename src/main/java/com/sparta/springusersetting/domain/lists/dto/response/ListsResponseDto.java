package com.sparta.springusersetting.domain.lists.dto.response;

import com.sparta.springusersetting.domain.card.dto.ListsCardResponseDto;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ListsResponseDto {
    private Long id;
    private String title;
    private Integer pos;
    private List<ListsCardResponseDto> cardList;

    public ListsResponseDto(Lists lists){
        this.id = lists.getId();
        this.title = lists.getTitle();
        this.pos = lists.getPos();
        this.cardList = lists.getCardList().stream()
                .map(ListsCardResponseDto::new)
                .collect(Collectors.toList());
    }
}
