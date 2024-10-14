package com.sparta.springusersetting.domain.participation.service;

import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.exception.BadAccessParticipationException;
import com.sparta.springusersetting.domain.participation.exception.NotFoundParticipationException;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.EmailResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.springusersetting.domain.user.enums.MemberRole.ROLE_WORKSPACE_ADMIN;
import static com.sparta.springusersetting.domain.user.enums.UserRole.ROLE_ADMIN;

@Service
public class ParticipationService {

    private final WorkspaceService workspaceService;
    private final UserService userService;

    private final ParticipationRepository participationRepository;

    public ParticipationService(ParticipationRepository participationRepository, WorkspaceService workspaceService, UserService userService) {
        this.participationRepository = participationRepository;
        this.workspaceService = workspaceService;
        this.userService = userService;
    }

    @Transactional
    public String setWorkspaceAdmin(Long userId, Long workspaceId) {

        User user = userService.findUser(userId);
        if(user.getUserRole()==ROLE_ADMIN){
            throw new BadAccessUserException();
        }
        Workspace workspace =  workspaceService.findWorkspace(workspaceId);


        // 찾고 없으면 생성
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(user.getId(), workspace.getId())
                .orElseGet(() -> new Participation(user, workspace));

        // 관리자 권한 부여
        if(participation.getMemberRole()==ROLE_WORKSPACE_ADMIN){
            throw new BadAccessParticipationException();
        }
        participation.UserBeADMIN();

        // 활성화
        if(!participation.isActivation()){
            participation.UserBeActive();
        }
        participationRepository.save(participation);

        return "관리자 설정 완료";
    }

    // 유저가 해당 워크스페이스에 포함되어 있는지
    public Participation isMember(Long userId, Long workspaceId){
        return participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(NotFoundUserException::new);
    }



    @Transactional
    public void inviteToWorkspace(User user, Workspace workspace){
        Participation participation = new Participation(user, workspace);
        participationRepository.save(participation);
    }

    public MemberRole checkMemberRole(Long userId, Long workspaceId){
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(NotFoundParticipationException::new);
        return participation.getMemberRole();
    }

    @Transactional
    public void deleteWorkspace(Participation participation) {
        participationRepository.delete(participation);
    }



    public EmailResponseDto inviteUserToWorkspace(User user, Long workspaceId, Long userId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId)!= MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }
        // 워크 스페이스와 유저 존재 체크
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        User newUser = userService.findUser(userId);

        // 워크 스페이스에 초대
        inviteToWorkspace(newUser, workspace);

        return new EmailResponseDto(newUser.getEmail());
    }

    @Transactional
    public String callYes(User user,Long workspaceId) {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        Participation participation = isMember(user.getId(), workspaceId);

        // activation을 true로 전환
        participation.UserBeActive();

        return "초대 수락 완료";
    }

    @Transactional
    public String callNo(User user,Long workspaceId) {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        Participation participation = isMember(user.getId(), workspaceId);

        // 중간 테이블에서 삭제
        deleteWorkspace(participation);

        return "초대 거절 완료";
    }

    public Page<WorkspaceResponseDto> viewOwnWorkspace(int page, int size, User user) {
        // 페이징
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Participation> workspaces = participationRepository.findAllByUserId(pageable, user.getId());

        return workspaces.map(participation -> new WorkspaceResponseDto(
                participation.getWorkspace().getName(),
                participation.getWorkspace().getDescription()
        ));
    }

    @Transactional
    public UpdateWorkspaceResponseDto updateWorkspace(User user, WorkspaceRequestDto workspaceRequestDto, Long workspaceId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId)!= MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }
        // 수정 하고 저장
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        workspace.updateWorkspace(workspaceRequestDto.getName(),workspaceRequestDto.getDescription());

        return new UpdateWorkspaceResponseDto(workspace.getId(), workspace.getName(), workspace.getDescription());

    }

    @Transactional
    public DeleteWorkspaceResponseDto deleteWorkspace(User user, Long workspaceId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId)!= MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }
        // 날려
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        workspaceService.deleteWorkspace(workspace);

        return new DeleteWorkspaceResponseDto(workspace.getId(), workspace.getName());
    }

}
