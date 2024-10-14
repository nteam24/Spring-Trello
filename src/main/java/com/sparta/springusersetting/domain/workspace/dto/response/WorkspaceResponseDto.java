package com.sparta.springusersetting.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class WorkspaceResponseDto {

    private final String name;
    private final String description;

    public WorkspaceResponseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
