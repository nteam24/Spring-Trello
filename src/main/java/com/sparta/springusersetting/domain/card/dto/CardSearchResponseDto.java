package com.sparta.springusersetting.domain.card.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.springusersetting.domain.card.entity.Card;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CardSearchResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
    private String userEmail;
    private LocalDateTime createdAt;

    public CardSearchResponseDto(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.contents = card.getContents();
        this.deadline = card.getDeadline();
        this.userEmail = card.getManager().getEmail();
    }

    @QueryProjection
    public CardSearchResponseDto(Long id, String title, String contents, LocalDate deadline, String userEmail, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }
}
