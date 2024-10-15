package com.sparta.springusersetting.domain.card.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardRequestDto {

    private Long managerId;
    private Long listId;
    private String title;
    private String contents;
    private LocalDate deadline;

}
