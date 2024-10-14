package com.sparta.springusersetting.domain.comment.entity;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.enums.CommentEmoji;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @Enumerated(EnumType.STRING)
    private CommentEmoji commentEmoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    public Comment(User user, Card card, CommentRequestDto requestDto) {
        this.user = user;
        this.card = card;
        this.content = requestDto.getContent();
        this.commentEmoji = requestDto.getCommentEmoji();
    }

    // 댓글 수정
    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.commentEmoji = requestDto.getCommentEmoji();
    }
}
