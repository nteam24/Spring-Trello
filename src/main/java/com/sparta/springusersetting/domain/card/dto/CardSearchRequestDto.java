package com.sparta.springusersetting.domain.card.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CardSearchRequestDto {
    private Long workspaceId;
    private Long boardId;
    private String keyword;
    private LocalDate deadline;
    private String managerEmail;
}
