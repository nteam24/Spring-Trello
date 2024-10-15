package com.sparta.springusersetting.domain.lists.dto.response;

import com.sparta.springusersetting.domain.lists.entity.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsCreateResponseDto {
    private Long id;
    private String title;
    private Integer pos;

    public ListsCreateResponseDto(Lists lists){
        this.id = lists.getId();
        this.title = lists.getTitle();
        this.pos = lists.getPos();
    }
}
