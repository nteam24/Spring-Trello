package com.sparta.springusersetting.domain.comment.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.service.CommentService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private CommentService commentService;

    // 댓글 등록
    @PostMapping("todo/{todoId}/comment")
    public ResponseEntity<ApiResponse<?>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(commentService.createComment(authUser.getUserId(), cardId, requestDto)));
    }

}
