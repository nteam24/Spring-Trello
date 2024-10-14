package com.sparta.springusersetting.domain.participation.service;

import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.exception.NotFoundParticipationException;
import com.sparta.springusersetting.domain.participation.repository.ParticipationRepository;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceManageService {

    private final WorkspaceService workspaceService;

    private final ParticipationRepository participationRepository;

    public WorkspaceManageService(WorkspaceService workspaceService, ParticipationRepository participationRepository) {
        this.workspaceService = workspaceService;
        this.participationRepository = participationRepository;
    }

    // 소속 워크 스페이스 조회하기
    public Page<WorkspaceResponseDto> viewOwnWorkspace(int page, int size, User user) {
        // 페이징
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Participation> workspaces = participationRepository.findAllByUserId(pageable, user.getId());

        return workspaces.map(participation -> new WorkspaceResponseDto(
                participation.getWorkspace().getName(),
                participation.getWorkspace().getDescription()
        ));
    }

    // 워크 스페이스 수정하기
    @Transactional
    public UpdateWorkspaceResponseDto updateWorkspace(User user, WorkspaceRequestDto workspaceRequestDto, Long workspaceId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId) != MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }

        // 수정 하고 저장
        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        workspace.updateWorkspace(workspaceRequestDto.getName(), workspaceRequestDto.getDescription());

        return new UpdateWorkspaceResponseDto(workspace.getId(), workspace.getName(), workspace.getDescription());

    }

    // 워크 스페이스 삭제하기
    @Transactional
    public DeleteWorkspaceResponseDto deleteWorkspace(User user, Long workspaceId) {
        // 유저가 관리자인지 체크
        if (checkMemberRole(user.getId(), workspaceId) != MemberRole.ROLE_WORKSPACE_ADMIN) {
            throw new BadAccessUserException();
        }

        Workspace workspace = workspaceService.findWorkspace(workspaceId);
        workspaceService.deleteWorkspace(workspace);

        return new DeleteWorkspaceResponseDto(workspace.getId(), workspace.getName());
    }

    // 멤버 권한 체크하기
    public MemberRole checkMemberRole(Long userId, Long workspaceId) {
        Participation participation = participationRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(NotFoundParticipationException::new);
        return participation.getMemberRole();
    }


}
