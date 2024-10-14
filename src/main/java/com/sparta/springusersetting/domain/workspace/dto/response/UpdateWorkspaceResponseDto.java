package com.sparta.springusersetting.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class UpdateWorkspaceResponseDto {

    private final Long id;
    private final String name;
    private final String description;


    public UpdateWorkspaceResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
