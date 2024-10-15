package com.sparta.springusersetting.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class EmailResponseDto {

    private final String email;


    public EmailResponseDto(String email) {
        this.email = email;
    }
}
