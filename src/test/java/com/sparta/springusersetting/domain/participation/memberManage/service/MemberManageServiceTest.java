package com.sparta.springusersetting.domain.participation.memberManage.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.exception.BadAccessParticipationException;
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

import static com.sparta.springusersetting.domain.user.enums.MemberRole.ROLE_WORKSPACE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class MemberManageServiceTest {

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
        workspace = mock(Workspace.class);
        ReflectionTestUtils.setField(workspace,"id",1L);
        participation = mock(Participation.class);
        ReflectionTestUtils.setField(participation, "id", 1L);
        ReflectionTestUtils.setField(participation, "activation", false);
    }

    @Test
    void 관리자_등록_성공(){
        // given
        given(userService.findUser(anyLong())).willReturn(normalUser);
        given(workspaceService.findWorkspace(anyLong())).willReturn((workspace));
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(participation.getMemberRole()).willReturn(MemberRole.ROLE_READ_USER);

        // when
        String response = memberManageService.setWorkspaceAdmin(1L, 1L);

        // then
        assertThat(response).isEqualTo("관리자 설정 완료");
    }

    @Test
    void 관리자_등록_실패_일반유저아님(){

        given(userService.findUser(anyLong())).willReturn(adminUser);
        given(adminUser.getUserRole()).willReturn(UserRole.ROLE_ADMIN);

        BadAccessUserException exception = assertThrows(BadAccessUserException.class, () -> {
            memberManageService.setWorkspaceAdmin(1L, 1L);
        });

        assertEquals("400 BAD_REQUEST INVALID_ROLE 올바른 권한이 아닙니다.",exception.getMessage());
    }

    @Test
    void 관리자_등록_실패_이미_관리자(){
        given(userService.findUser(anyLong())).willReturn(normalUser);
        given(workspaceService.findWorkspace(anyLong())).willReturn((workspace));
        given(participationRepository.findByUserIdAndWorkspaceId(anyLong(), anyLong())).willReturn(Optional.of(participation));
        given(participation.getMemberRole()).willReturn(ROLE_WORKSPACE_ADMIN);

        BadAccessParticipationException exception = assertThrows(BadAccessParticipationException.class, () -> {
            memberManageService.setWorkspaceAdmin(1L, 1L);
        });

        assertEquals("400 BAD_REQUEST ALREADY_ADMIN 이미 해당 워크스페이스의 관리자 입니다.",exception.getMessage());
    }
}
