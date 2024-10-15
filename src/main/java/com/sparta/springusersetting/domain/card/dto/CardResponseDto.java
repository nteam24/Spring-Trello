package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.model.IComment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
    private String userEmail;
    private List<ActivityLogResponseDto> activityLogs;
    private List<CommentResponseDto> commentList;

    public CardResponseDto(Long id, String title, String contents, LocalDate deadline, String userEmail, List<ActivityLogResponseDto> activityLogs, List<Comment> commentList)
    {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.userEmail = userEmail;
        this.activityLogs = activityLogs;
        this.commentList = commentList.stream()
                .map(CommentResponseDto::new)
                .toList();
    }
}
