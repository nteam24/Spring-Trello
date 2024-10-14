package com.sparta.springusersetting.domain.board.dto.request;

import lombok.Getter;

@Getter
public class BoardRequestDto {

    private String title;
    private String backgroundColor;
    private String backgroundImageUrl;
    private Long workspaceId;  // 워크스페이스 ID
}
