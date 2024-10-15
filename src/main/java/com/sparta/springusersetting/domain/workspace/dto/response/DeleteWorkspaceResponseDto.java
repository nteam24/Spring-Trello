package com.sparta.springusersetting.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class DeleteWorkspaceResponseDto {

    private final Long id;
    private final String name;


    public DeleteWorkspaceResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
