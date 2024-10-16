package com.sparta.springusersetting.domain.comment.dto.response;

import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.comment.enums.CommentEmoji;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

    private String content;
    @Enumerated(EnumType.STRING)
    private CommentEmoji commentEmoji;
    private Long commentViewCount;

    public CommentResponseDto(Comment comment) {
        this.content = comment.getContent();
        this.commentEmoji = comment.getCommentEmoji();
    }

    public CommentResponseDto(Comment comment, Long commentViewCount) {
        this.content = comment.getContent();
        this.commentEmoji = comment.getCommentEmoji();
        this.commentViewCount = commentViewCount;
    }
}
