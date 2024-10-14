package com.sparta.springusersetting.domain.comment.dto.request;

import com.sparta.springusersetting.domain.comment.enums.CommentEmoji;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequestDto {
    private String content;
    private CommentEmoji commentEmoji;
}
