package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.card.entity.Card;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class ListsCardResponseDto {
    private String title;
    private LocalDate deadline;

    public ListsCardResponseDto(Card card){
        this.title = card.getTitle();
        this.deadline = card.getDeadline();
    }
}
