package com.sparta.springusersetting.domain.participation.memberManage.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MemberInviteTest {

    @Mock
    private WorkspaceService workspaceService;
    @Mock
    private UserService userService;
    @Mock
    private ParticipationRepository participationRepository;

    @InjectMocks
    private MemberManageService memberManageService;

    private AuthUser authUser;
    private User adminUser;
    private User normalUser;
    private Workspace workspace;
    private Participation participation;

    @BeforeEach
    void setUp(){
        authUser = mock(AuthUser.class);
        ReflectionTestUtils.setField(authUser,"userId",1L);
        adminUser = mock(User.class);
        ReflectionTestUtils.setField(adminUser,"id",1L);
        ReflectionTestUtils.setField(adminUser,"userRole", UserRole.ROLE_ADMIN);
        normalUser = mock(User.class);
        ReflectionTestUtils.setField(normalUser,"id",2L);
        ReflectionTestUtils.setField(normalUser,"userRole", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(normalUser,"email", "test1@gmail.com");
        workspace = mock(Workspace.class);
        ReflectionTestUtils.setField(workspace,"id",1L);
        participation = mock(Participation.class);
        ReflectionTestUtils.setField(participation, "id", 1L);
        ReflectionTestUtils.setField(participation, "activation", false);
    }

    @Test
    void 유저_초대_성공(){
        // Arrange
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(memberManageService.checkMemberRole(1L, 1L)).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
        given(workspaceService.findWorkspace(1L)).willReturn(workspace);
        given(userService.findUser(2L)).willReturn(normalUser);

        // Act
        String response = memberManageService.inviteUserToWorkspace(adminUser, 1L, 2L);

        // Assert
        assertThat(response).isEqualTo("초대 성공");
    }

    @Test
    void 유저_초대_실패_유저_비관리자(){
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(memberManageService.checkMemberRole(1L, 1L)).willReturn(MemberRole.ROLE_READ_USER);

        BadAccessUserException exception = assertThrows(BadAccessUserException.class,()->{
            memberManageService.inviteUserToWorkspace(adminUser, 1L, 2L);
        });

        assertEquals("400 BAD_REQUEST INVALID_ROLE 올바른 권한이 아닙니다.",exception.getMessage());
    }

    @Test
    void 유저_초대_수락하기(){
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));

        String response = memberManageService.callYes(adminUser, 1L);

        assertThat(response).isEqualTo("초대 수락 완료");
    }

    @Test
    void 유저_초대_거절하기(){
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));

        String response = memberManageService.callNo(adminUser, 1L);

        assertThat(response).isEqualTo("초대 거절 완료");
    }

    @Test
    void 멤버_권한_변경_성공(){
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(participation.getMemberRole()).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);

        String response = memberManageService.changeMemberRole(adminUser, 1L, 2L,"ROLE_WORKSPACE_ADMIN");

        assertThat(response).isEqualTo("권한 변경 완료");
    }

    @Test
    void 멤버_권한_변경_실패(){
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(participation.getMemberRole()).willReturn(MemberRole.ROLE_READ_USER);

        BadAccessUserException exception = assertThrows(BadAccessUserException.class, () -> {
            memberManageService.changeMemberRole(adminUser, 1L, 2L,"ROLE_WORKSPACE_ADMIN");
        });

        assertEquals("400 BAD_REQUEST INVALID_ROLE 올바른 권한이 아닙니다.",exception.getMessage());
    }

}
