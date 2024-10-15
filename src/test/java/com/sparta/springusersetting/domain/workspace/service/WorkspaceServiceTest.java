package com.sparta.springusersetting.domain.workspace.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.exception.DuplicateWorkspaceException;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @InjectMocks
    private WorkspaceService workspaceService;

    @Mock
    private  WorkspaceRepository workspaceRepository;

    private AuthUser authUser;
    private User user;
    private Workspace workspace;

    @BeforeEach
    void setUp(){
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser,"userId",1L);
        user = mock(User.class);
        ReflectionTestUtils.setField(user,"id",1L);
        ReflectionTestUtils.setField(user,"userRole", UserRole.ROLE_ADMIN);
        workspace = mock(Workspace.class);
        ReflectionTestUtils.setField(workspace,"id",1L);
    }

    @Test
    void 워크스페이스_생성_성공(){
        // given
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("test1", "test description1");

        given(workspaceRepository.findByName(anyString())).willReturn(Optional.empty());  // 워크스페이스가 없다고 설정
        given(workspaceRepository.save(any(Workspace.class))).willReturn(workspace);

        // when
        WorkspaceResponseDto response = workspaceService.createWorkspace(workspaceRequestDto);

        // then
        assertThat(response.getName()).isEqualTo("test1");
    }

    @Test
    void 워크스페이스_생성_실패_이미_존재(){
        // given
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("test1", "test description1");

        given(workspaceRepository.findByName(anyString())).willReturn(Optional.of(workspace));

        // when
        DuplicateWorkspaceException exception = assertThrows(DuplicateWorkspaceException.class, () -> {
            workspaceService.createWorkspace(workspaceRequestDto);
        });

        // then
        assertEquals("409 CONFLICT DUPLICATE_WORKSPACE_NAME 중복된 워크스페이스명 입니다.",exception.getMessage());
    }

    @Test
    void 워크스페이스_id로_스페이스_반환_성공(){
        // Given
        given(workspaceRepository.findById(1L)).willReturn(Optional.of(workspace));

        // When
        Workspace response = workspaceService.findWorkspace(1L);

        // Then
        assertEquals(workspace.getId(), response.getId()); // ID 비교
    }
    @Test
    void 워크스페이스_id로_스페이스_반환_실패(){
        // Given
        given(workspaceRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        NotFoundWorkspaceException exception = assertThrows(NotFoundWorkspaceException.class, () -> {
            workspaceService.findWorkspace(1L);
        });

        // Then
        assertEquals("404 NOT_FOUND NOT_FOUND_WORKSPACE 워크스페이스가 존재하지 않습니다.",exception.getMessage());
    }

    @Test
    void 워크스페이스_삭제_성공() {
        // When
        workspaceService.deleteWorkspace(workspace);

        // Then
        verify(workspaceRepository).delete(workspace); // delete 메서드가 호출되었는지 검증
    }




}
