package com.sparta.springusersetting.domain.card.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class CardRequestDto {

    private Long managerId;
    private Long listId;
    private String title;
    private String contents;
    private LocalDate deadline;

//    public CardRequestDto(long l, long l1, String s, String s1, LocalDate localDate) {
//    }
}
