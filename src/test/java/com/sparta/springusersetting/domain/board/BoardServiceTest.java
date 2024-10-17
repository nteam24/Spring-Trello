package com.sparta.springusersetting.domain.board.service;

import com.sparta.springusersetting.domain.board.dto.request.BoardRequestDto;
import com.sparta.springusersetting.domain.board.dto.response.BoardResponseDto;
import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.board.exception.InvalidBackgroundException;
import com.sparta.springusersetting.domain.board.exception.NotFoundBoardException;
import com.sparta.springusersetting.domain.board.exception.NotFoundTitleException;
import com.sparta.springusersetting.domain.board.exception.UnauthorizedActionException;
import com.sparta.springusersetting.domain.board.repository.BoardRepository;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.sparta.springusersetting.domain.workspace.entity.QWorkspace.workspace;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ParticipationRepository participationRepository;

    private Participation participation;
    private BoardRequestDto boardRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Workspace workspace = new Workspace();
        ReflectionTestUtils.setField(workspace, "id", 1L);

        Board board = new Board("Existing Board Title", "blue", null, workspace);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        participation = new Participation();
        ReflectionTestUtils.setField(participation, "memberRole", MemberRole.ROLE_WORKSPACE_ADMIN);
        ReflectionTestUtils.setField(participation, "workspace", workspace);
        boardRequestDto = new BoardRequestDto();
        ReflectionTestUtils.setField(boardRequestDto,"title", "test board");
        ReflectionTestUtils.setField(boardRequestDto,"backgroundColor", "blue");
        ReflectionTestUtils.setField(boardRequestDto,"backgroundImageUrl", "http://example.com/image.png");

        ReflectionTestUtils.setField(boardService, "boardRepository", boardRepository);
        ReflectionTestUtils.setField(boardService, "participationRepository", participationRepository);
    }

    @Test
    void createBoard_Success() {
        when(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).thenReturn(Optional.of(participation));

        BoardResponseDto response = boardService.createBoard(boardRequestDto, 1L, 1L);

        assertNotNull(response);
        assertEquals("test board", response.getTitle());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void createBoard_TitleNotFound() {
        ReflectionTestUtils.setField(boardRequestDto, "title", null);

        assertThrows(NotFoundTitleException.class, () -> boardService.createBoard(boardRequestDto, 1L, 1L));
    }

    @Test
    void createBoard_InvalidBackground() {
        ReflectionTestUtils.setField(boardRequestDto, "backgroundColor", null);
        ReflectionTestUtils.setField(boardRequestDto, "backgroundImageUrl", null);

        assertThrows(InvalidBackgroundException.class, () -> boardService.createBoard(boardRequestDto, 1L, 1L));
    }

//    @Test
//    void updateBoard_Success() {
//        // Mock Board 객체 생성 및 설정
//        Board board = new Board();
//        ReflectionTestUtils.setField(board, "id", 1L);
//        ReflectionTestUtils.setField(board, "title", "기존 보드 제목");
//        ReflectionTestUtils.setField(board, "backgroundColor", "blue");
//        ReflectionTestUtils.setField(board, "backgroundImageUrl", null);
//
//        // 저장소 동작 모킹
//        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
//
//        // Participation mock 설정
//        Participation participation = new Participation();
//        ReflectionTestUtils.setField(participation, "memberRole", MemberRole.ROLE_WORKSPACE_ADMIN);
//        ReflectionTestUtils.setField(participation, "workspace", new Workspace());
//
//        // Participation 저장소 동작 모킹
//        when(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong()))
//                .thenReturn(Optional.of(participation));
//
//        // 업데이트할 BoardRequestDto 준비
//        BoardRequestDto boardRequestDto = new BoardRequestDto();
//        ReflectionTestUtils.setField(boardRequestDto, "title", "테스트 보드");
//        ReflectionTestUtils.setField(boardRequestDto, "backgroundColor", "green");
//        ReflectionTestUtils.setField(boardRequestDto, "backgroundImageUrl", "http://example.com/newimage.png");
//
//        // 보드 업데이트 메서드 호출
//        BoardResponseDto response = boardService.updateBoard(1L, boardRequestDto, 1L, 1L);
//
//        // 결과 검증
//        assertNotNull(response);
//        assertEquals("테스트 보드", response.getTitle()); // 업데이트된 제목 확인
//        assertEquals("green", response.getBackgroundColor()); // 업데이트된 배경색 확인
//        assertEquals("http://example.com/newimage.png", response.getBackgroundImageUrl()); // 업데이트된 이미지 URL 확인
//
//        // findById 호출 확인
//        verify(boardRepository, times(1)).findById(1L);
//        // 업데이트 후 board가 저장되었는지 확인
//        verify(boardRepository, times(1)).save(board);
//
//        // 보드 속성이 예상대로 업데이트되었는지 확인
//        assertEquals("테스트 보드", board.getTitle()); // 제목이 업데이트되었는지 확인
//        assertEquals("green", board.getBackgroundColor()); // 배경색이 업데이트되었는지 확인
//        assertEquals("http://example.com/newimage.png", board.getBackgroundImageUrl()); // 이미지 URL이 업데이트되었는지 확인
//    }
//
//    @Test
//    void updateBoard_NotFound() {
//        when(boardRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundBoardException.class, () -> boardService.updateBoard(1L, boardRequestDto, 1L, 1L));
//    }

    @Test
    void deleteBoard_Success() {
        Board board = new Board();
        ReflectionTestUtils.setField(board, "id", 1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).thenReturn(Optional.of(participation));

        boardService.deleteBoard(1L, 1L, 1L);

        verify(boardRepository, times(1)).delete(board);
    }

//    @Test
//    void deleteBoard_NotFound() {
//        when(boardRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundBoardException.class, () -> boardService.deleteBoard(1L, 1L, 1L));
//    }
//
//    @Test
//    void getBoard_Success() {
//        Board board = new Board();
//        ReflectionTestUtils.setField(board, "id", 1L);
//        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
//
//        BoardResponseDto response = boardService.getBoard(1L);
//
//        assertNotNull(response);
//        assertEquals(1L, response.getId());
//    }

    @Test
    void getBoard_NotFound() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundBoardException.class, () -> boardService.getBoard(1L));
    }
}