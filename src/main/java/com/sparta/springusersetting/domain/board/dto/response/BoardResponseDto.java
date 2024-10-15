package com.sparta.springusersetting.domain.board.dto.response;

import com.sparta.springusersetting.domain.board.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponseDto {

    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImageUrl;
    private Long workspaceId;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.backgroundColor = board.getBackgroundColor();
        this.backgroundImageUrl = board.getBackgroundImageUrl();
        this.workspaceId = board.getWorkspace().getId();
    }
}
