package com.sparta.springusersetting.domain.board.service;

import com.sparta.springusersetting.domain.board.dto.request.BoardRequestDto;
import com.sparta.springusersetting.domain.board.dto.response.BoardResponseDto;
import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.board.exception.NotFoundBoardException;
import com.sparta.springusersetting.domain.board.exception.UnauthorizedActionException;
import com.sparta.springusersetting.domain.board.repository.BoardRepository;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import com.sparta.springusersetting.domain.userWorkspace.repository.UserWorkspaceRepository;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    // 보드 등록
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, Long userId, Long workspaceId) {
        UserWorkspace userWorkspace =  validateCreateBoard(userId, workspaceId);

        Board board = new Board(
                boardRequestDto.getTitle(),
                boardRequestDto.getBackgroundColor(),
                boardRequestDto.getBackgroundImageUrl(),
                userWorkspace.getWorkspace()
        );
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // 보드 수정
    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto, Long userId, Long workspaceId) {
        Board board = validateUpdateBoard(boardId, userId, workspaceId);

        board.updateBoard(
                boardRequestDto.getTitle(),
                boardRequestDto.getBackgroundColor(),
                boardRequestDto.getBackgroundImageUrl()
                );
        return new BoardResponseDto(board);
    }

    // 보드 조회 (단건)
    @Transactional(readOnly = true)
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));
        return new BoardResponseDto(board);
    }


    // 보드 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoardsByWorkspace(Long workspaceId) {
        List<Board> boards = boardRepository.findByWorkspaceId(workspaceId);
        return boards.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 보드 삭제
    @Transactional
    public void deleteBoard(Long boardId, Long userId, Long workspaceId) {
        Board board = validateDeleteBoard(boardId, userId, workspaceId);

        boardRepository.delete(board);
    }

    // 보드 생성 검증 로직
    private UserWorkspace validateCreateBoard(Long userId, Long workspaceId) {
        UserWorkspace userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (userWorkspace.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        return userWorkspace;
    }

    // 보드 수정 검증 로직
    private Board validateUpdateBoard(Long boardId, Long userId, Long workspaceId) {
        UserWorkspace userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (userWorkspace.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());
        return board;
    }

    // 보드 삭제 검증 로직
    private Board validateDeleteBoard(Long boardId, Long userId, Long workspaceId) {
        UserWorkspace userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (userWorkspace.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());
        return board;
    }
}

