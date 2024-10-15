package com.sparta.springusersetting.domain.board.controller;

import com.sparta.springusersetting.domain.board.dto.request.BoardRequestDto;
import com.sparta.springusersetting.domain.board.dto.response.BoardResponseDto;
import com.sparta.springusersetting.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @RequestBody BoardRequestDto boardRequestDto,
            @RequestParam Long userId,
            @RequestParam Long wordspaceId) {
        return ResponseEntity.ok(boardService.createBoard(boardRequestDto, userId, wordspaceId));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> updateBoard(
            @PathVariable Long boardId,
            @RequestBody BoardRequestDto boardRequestDto,
            @RequestParam Long userId,
            @RequestParam Long workspaceId) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, boardRequestDto, userId, workspaceId));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoard(
            @PathVariable Long boardId
    ) {
        BoardResponseDto board = boardService.getBoard(boardId);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<BoardResponseDto>> getBoardByWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(boardService.getBoardsByWorkspace(workspaceId));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId,
            @RequestParam Long userId,
            @RequestParam Long workspaceId
            ){
        boardService.deleteBoard(boardId, userId, workspaceId);
        return ResponseEntity.noContent().build();
    }
}
