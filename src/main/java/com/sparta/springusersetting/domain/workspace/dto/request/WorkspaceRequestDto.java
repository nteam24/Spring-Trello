package com.sparta.springusersetting.domain.workspace.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceRequestDto {

    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
