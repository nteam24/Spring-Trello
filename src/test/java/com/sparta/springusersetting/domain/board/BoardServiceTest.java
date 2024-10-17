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

    @Test
    void deleteBoard_Success() {
        Board board = new Board();
        ReflectionTestUtils.setField(board, "id", 1L);
        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
        when(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).thenReturn(Optional.of(participation));

        boardService.deleteBoard(1L, 1L, 1L);

        verify(boardRepository, times(1)).delete(board);
    }

    @Test
    void getBoard_NotFound() {
        when(boardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundBoardException.class, () -> boardService.getBoard(1L));
    }
}