package com.sparta.springusersetting.domain.board.service;

import com.sparta.springusersetting.domain.board.dto.request.BoardRequestDto;
import com.sparta.springusersetting.domain.board.dto.response.BoardResponseDto;
import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.board.exception.InvalidBackgroundException;
import com.sparta.springusersetting.domain.board.exception.NotFoundBoardException;
import com.sparta.springusersetting.domain.board.exception.NotFoundTitleException;
import com.sparta.springusersetting.domain.board.exception.UnauthorizedActionException;
import com.sparta.springusersetting.domain.board.repository.BoardRepository;
import com.sparta.springusersetting.domain.common.service.S3Service;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final ParticipationRepository participationRepository;
    private final S3Service s3Service;

    // 보드 등록
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, Long userId, Long workspaceId) {
        Participation participation = validateCreateBoard(boardRequestDto,userId, workspaceId);

       String backgroundImageUrl = null; // 이미지 URL 초기화

//        // 이미지가 제공된 경우 S3에 업로드하고 URL을 받아옴
//        if (backgroundImage != null && !backgroundImage.isEmpty()) {
//            backgroundImageUrl = s3Service.uploadFile(backgroundImage); // S3에 업로드
//        }

        Board board = new Board(
                boardRequestDto.getTitle(),
                boardRequestDto.getBackgroundColor(),
                boardRequestDto.getBackgroundImageUrl(),
                participation.getWorkspace()
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
                .orElseThrow(() -> new NotFoundBoardException());
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
    private Participation validateCreateBoard(BoardRequestDto boardRequestDto,Long userId, Long workspaceId) {

        // 로그인하지 않은 사용자는 보드 생성 불가
        if (userId == null) {
            throw new NotFoundUserException();
        }

        // 제목이 비어있는 경우 예외 처리
        if (boardRequestDto.getTitle() == null || boardRequestDto.getTitle().trim().isEmpty()) {
            throw new NotFoundTitleException();
        }

        // 배경색과 배경이미지 중 하나만 선택 가능하도록 유효성 검사
        if ((boardRequestDto.getBackgroundColor() == null || boardRequestDto.getBackgroundColor().trim().isEmpty()) &&
                (boardRequestDto.getBackgroundImageUrl() == null || boardRequestDto.getBackgroundImageUrl().trim().isEmpty())) {
            throw new InvalidBackgroundException();
        }

        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (participation.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        return participation;
    }

    // 보드 수정 검증 로직
    private Board validateUpdateBoard(Long boardId, Long userId, Long workspaceId) {
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (participation.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());
        return board;
    }

    // 보드 삭제 검증 로직
    private Board validateDeleteBoard(Long boardId, Long userId, Long workspaceId) {
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new NotFoundWorkspaceException());
        if (participation.getMemberRole() == MemberRole.ROLE_READ_USER) {
            throw new UnauthorizedActionException();
        }
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());
        return board;
    }
}

