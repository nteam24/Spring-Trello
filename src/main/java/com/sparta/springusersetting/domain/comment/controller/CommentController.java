package com.sparta.springusersetting.domain.comment.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.service.CommentService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final RedisTemplate<String, Object> redisTemplate;

    // 댓글 등록
    @PostMapping("card/{cardId}/comment")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cardId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(commentService.createComment(authUser.getUserId(), cardId, requestDto)));
    }

    // 댓글 단건 조회 ( Redis Test 용 )
    @GetMapping("comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> getComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId) {

        return ResponseEntity.ok(ApiResponse.success(commentService.getComment(authUser.getUserId(), commentId)));
    }

    // 댓글 수정
    @PutMapping("comment/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(commentService.updateComment(authUser.getUserId(), commentId, requestDto)));
    }

    // 댓글 삭제
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(ApiResponse.success(commentService.deleteComment(authUser.getUserId(), commentId)));
    }
}
