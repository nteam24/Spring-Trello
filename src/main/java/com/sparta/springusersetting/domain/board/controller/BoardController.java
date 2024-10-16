package com.sparta.springusersetting.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.board.dto.request.BoardRequestDto;
import com.sparta.springusersetting.domain.board.dto.response.BoardResponseDto;
import com.sparta.springusersetting.domain.board.service.BoardService;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.common.service.S3Service;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 등록
    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> createBoard(
            @RequestBody BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId,
            @RequestParam("backgroundImage") MultipartFile backgroundImage
            ) throws IOException {

        BoardResponseDto boardResponseDto = boardService.createBoard(boardRequestDto,authUser.getUserId(), workspaceId, backgroundImage);
        return ResponseEntity.ok(ApiResponse.success(boardResponseDto));
    }

    // 보드 수정
    @PutMapping("/{boardId}/workspace/{workspaceId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto boardRequestDto,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId, boardRequestDto, authUser.getUserId(), workspaceId);
        return ResponseEntity.ok(ApiResponse.success(boardResponseDto));
    }

    // 보드 단건 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponseDto>> getBoard(
            @PathVariable Long boardId
    ) {
        BoardResponseDto board = boardService.getBoard(boardId);
        return ResponseEntity.ok(ApiResponse.success(board));
    }

    // 보드 리스트 조회
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<ApiResponse<List<BoardResponseDto>>> getBoardByWorkspace(@PathVariable Long workspaceId) {
        List<BoardResponseDto> boardResponseDto = boardService.getBoardsByWorkspace(workspaceId);
        return ResponseEntity.ok(ApiResponse.success(boardResponseDto));
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}/workspace/{workspaceId}")
    public ResponseEntity<ApiResponse> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId
            ){
        boardService.deleteBoard(boardId, authUser.getUserId(), workspaceId);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
