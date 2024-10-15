package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetListsCardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
    private String managerEmail;
    private List<ActivityLogResponseDto> activityLogs;
    private List<CommentResponseDto> comments;

    public GetListsCardResponseDto(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.contents = card.getContents();
        this.deadline = card.getDeadline();
        this.managerEmail = card.getManager().getEmail();
        this.activityLogs = card.getActivityLogs().stream()
                .map(ActivityLogResponseDto::new)
                .collect(Collectors.toList());
        this.comments = card.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }
}
