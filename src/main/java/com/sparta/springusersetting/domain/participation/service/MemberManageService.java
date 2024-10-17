package com.sparta.springusersetting.domain.participation.service;

import com.sparta.springusersetting.domain.notification.util.NotificationUtil;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.exception.BadAccessParticipationException;
import com.sparta.springusersetting.domain.participation.exception.NotFoundParticipationException;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static com.sparta.springusersetting.domain.user.enums.MemberRole.ROLE_WORKSPACE_ADMIN;
import static com.sparta.springusersetting.domain.user.enums.UserRole.ROLE_ADMIN;

@Service
public class MemberManageService {
    private final WorkspaceService workspaceService;

    private final UserService userService;

    private final ParticipationRepository participationRepository;

    private final NotificationUtil notificationUtil;

    public MemberManageService(WorkspaceService workspaceService, UserService userService, ParticipationRepository participationRepository, NotificationUtil notificationUtil) {
        this.workspaceService = workspaceService;
        this.userService = userService;
        this.participationRepository = participationRepository;
        this.notificationUtil = notificationUtil;
    }

    // 관리자 등록
    @Transactional
    public String setWorkspaceAdmin(Long userId, Long workspaceId) {

        User user = userService.findUser(userId);
        if (user.getUserRole() == ROLE_ADMIN) {
            throw new BadAccessUserException();
        }
        Workspace workspace = workspaceService.findWorkspace(workspaceId);


        // 찾고 없으면 생성
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(user.getId(), workspace.getId())
                .orElseGet(() -> new Participation(user, workspace));

        // 관리자 권한 부여
        if (participation.getMemberRole() == ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessParticipationException();
        }
        participation.changeRole(ROLE_WORKSPACE_ADMIN);

        // 활성화
        if (!participation.isActivation()) {
            participation.UserBeActive();
        }
        participationRepository.save(participation);

        return "관리자 설정 완료";
    }

    // 초대하기
    public String inviteUserToWorkspace(User user, Long workspaceId, Long userId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId) != MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }

        // 워크 스페이스와 유저 존재 체크
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        User newUser = userService.findUser(userId);

        // 워크 스페이스에 초대
        Participation participation = new Participation(newUser, workspace);
        participationRepository.save(participation);

        return "초대 성공";
    }

    // 수락하기
    @Transactional
    public String callYes(User user, Long workspaceId, Workspace workspace) throws IOException {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        Participation participation = isMember(user.getId(), workspaceId);

        // activation 을 true 로 전환
        participation.UserBeActive();

        // 멤버 등록 알림 전송
        notificationUtil.sendNotification("%s 님이 %s 워크스페이스에 참여하였습니다.", user.getUserName(),workspace.getName());

        return "초대 수락 완료";
    }

    // 거절하기
    @Transactional
    public String callNo(User user, Long workspaceId)  {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        Participation participation = isMember(user.getId(), workspaceId);

        // 중간 테이블에서 삭제
        participationRepository.delete(participation);

        return "초대 거절 완료";
    }

    // 권한 확인하기
    public MemberRole checkMemberRole(Long userId, Long workspaceId) {
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(NotFoundParticipationException::new);
        return participation.getMemberRole();
    }
    // 멤버 권한 변경
    @Transactional
    public String changeMemberRole(User user, Long workspaceId, Long userId, String memberRole) {
        if (checkMemberRole(user.getId(), workspaceId) != MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }

        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(NotFoundParticipationException::new);
        participation.changeRole(MemberRole.of(memberRole));

        return "권한 변경 완료";
    }
    // 유저가 해당 워크스페이스에 포함되어 있는지 확인하기
    public Participation isMember(Long userId, Long workspaceId) {
        return participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(NotFoundUserException::new);
    }
}
