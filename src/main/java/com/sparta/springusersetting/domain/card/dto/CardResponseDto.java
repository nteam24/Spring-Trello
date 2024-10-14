package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import lombok.Getter;
import org.thymeleaf.model.IComment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter

public class CardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private LocalDate deadline;
    private String userEmail;
    private String activityLog;
    private List<CommentResponseDto> commentList;

    public CardResponseDto(Long id, String title, String contents, LocalDate deadline, String userEmail, String activityLog, List<Comment> commentList)
    {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.userEmail = userEmail;
        this.activityLog = activityLog;
        this.commentList = commentList.stream().map(comment -> new commentResponseDto(comment.getContent(), comment.getEmoji()));
    }



}
