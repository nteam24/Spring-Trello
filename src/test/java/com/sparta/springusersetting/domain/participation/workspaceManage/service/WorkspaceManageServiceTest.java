//package com.sparta.springusersetting.domain.participation.workspaceManage.service;
//
//import com.sparta.springusersetting.domain.common.dto.AuthUser;
//import com.sparta.springusersetting.domain.participation.entity.Participation;
//import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
//import com.sparta.springusersetting.domain.participation.service.MemberManageService;
//import com.sparta.springusersetting.domain.participation.service.WorkspaceManageService;
//import com.sparta.springusersetting.domain.user.entity.User;
//import com.sparta.springusersetting.domain.user.enums.MemberRole;
//import com.sparta.springusersetting.domain.user.enums.UserRole;
//import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
//import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
//import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
//import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
//import com.sparta.springusersetting.domain.workspace.entity.Workspace;
//import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static com.sparta.springusersetting.domain.user.enums.MemberRole.ROLE_WORKSPACE_ADMIN;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.mock;
//
//@ExtendWith(MockitoExtension.class)
//public class WorkspaceManageServiceTest {
//
//    @Mock
//    private WorkspaceService workspaceService;
//    @Mock
//    private MemberManageService memberManageService;
//
//    @Mock
//    private ParticipationRepository participationRepository;
//
//    @InjectMocks
//    private WorkspaceManageService workspaceManageService;
//
//    private AuthUser authUser;
//    private User adminUser;
//    private User normalUser;
//    private Workspace workspace;
//    private Participation participation;
//
//    @BeforeEach
//    void setUp(){
//        authUser = mock(AuthUser.class);
//        ReflectionTestUtils.setField(authUser,"userId",1L);
//        adminUser = mock(User.class);
//        ReflectionTestUtils.setField(adminUser,"id",1L);
//        ReflectionTestUtils.setField(adminUser,"userRole", UserRole.ROLE_ADMIN);
//        normalUser = mock(User.class);
//        ReflectionTestUtils.setField(normalUser,"id",2L);
//        ReflectionTestUtils.setField(normalUser,"userRole", UserRole.ROLE_USER);
//        ReflectionTestUtils.setField(normalUser,"email", "test1@gmail.com");
//        participation = mock(Participation.class);
//        ReflectionTestUtils.setField(participation, "id", 1L);
//        ReflectionTestUtils.setField(participation, "activation", false);
//    }
//
//    @Test
//    void 워크_스페이스_수정_성공(){
//        // Given
//        Long workspaceId = 1L;
//        Long userId = 1L;
//
//        WorkspaceRequestDto requestDto = new WorkspaceRequestDto();
//        ReflectionTestUtils.setField(requestDto, "name", "Updated Name");
//        ReflectionTestUtils.setField(requestDto, "description", "Updated Description");
//
//        Workspace workspace = new Workspace();
//        ReflectionTestUtils.setField(workspace, "id", workspaceId);
//        ReflectionTestUtils.setField(workspace, "name", "Old Name");
//        ReflectionTestUtils.setField(workspace, "description", "Old Description");
//
//        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
//        given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
//        given(workspaceService.findWorkspace(workspaceId)).willReturn(workspace);
//
//        // When
//        UpdateWorkspaceResponseDto response = workspaceManageService.updateWorkspace(adminUser, requestDto, workspaceId);
//
//        // Then
//        assertNotNull(response);
//        assertEquals("Updated Name", response.getName());
//    }
//
//    @Test
//    void 워크_스페이스_수정_실패(){
//        Long workspaceId = 1L;
//
//        WorkspaceRequestDto requestDto = new WorkspaceRequestDto();
//        ReflectionTestUtils.setField(requestDto, "name", "Updated Name");
//        ReflectionTestUtils.setField(requestDto, "description", "Updated Description");
//
//        Workspace workspace = new Workspace();
//        ReflectionTestUtils.setField(workspace, "id", workspaceId);
//        ReflectionTestUtils.setField(workspace, "name", "Old Name");
//        ReflectionTestUtils.setField(workspace, "description", "Old Description");
//
//        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
//        given(participation.getMemberRole()).willReturn(MemberRole.ROLE_READ_USER);
//
//        BadAccessUserException exception = assertThrows(BadAccessUserException.class, () -> {
//            workspaceManageService.updateWorkspace(adminUser, requestDto, workspaceId);
//        });
//
//        assertEquals("400 BAD_REQUEST INVALID_ROLE 올바른 권한이 아닙니다.",exception.getMessage());
//    }
//
//    @Test
//    void 워크_스페이스_삭제_성공(){
//        // Given
//        Long workspaceId = 1L;
//        Long userId = 1L;
//
//        Workspace workspace = new Workspace();
//        ReflectionTestUtils.setField(workspace, "id", workspaceId); // 필드 직접 설정
//        ReflectionTestUtils.setField(workspace, "name", "Test Workspace"); // 필드 직접 설정
//
//        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
//        given(memberManageService.checkMemberRole(userId, workspaceId)).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
//        given(workspaceService.findWorkspace(workspaceId)).willReturn(workspace);
//
//        // When
//        DeleteWorkspaceResponseDto response = workspaceManageService.deleteWorkspace(adminUser, workspaceId);
//
//        // Then
//        assertNotNull(response);
//        assertEquals(workspaceId, response.getId());
//
//    }
//
//    @Test
//    void 워크_스페이스_삭제_실패(){
//        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
//        given(participation.getMemberRole()).willReturn(MemberRole.ROLE_READ_USER);
//
//        BadAccessUserException exception = assertThrows(BadAccessUserException.class, () -> {
//            workspaceManageService.deleteWorkspace(adminUser,1L);
//        });
//
//        assertEquals("400 BAD_REQUEST INVALID_ROLE 올바른 권한이 아닙니다.",exception.getMessage());
//
//    }
//
//
//}
