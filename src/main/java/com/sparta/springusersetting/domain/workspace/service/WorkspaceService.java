package com.sparta.springusersetting.domain.workspace.service;

import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import com.sparta.springusersetting.domain.userWorkspace.repository.UserWorkspaceRepository;
import com.sparta.springusersetting.domain.userWorkspace.service.UserWorkspaceService;
import com.sparta.springusersetting.domain.workspace.dto.request.DeleteWorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.EmailResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_WORKSPACE;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceService userWorkspaceService;

    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository, UserWorkspaceService userWorkspaceService, UserRepository userRepository, UserWorkspaceRepository userWorkspaceRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userWorkspaceService = userWorkspaceService;
        this.userRepository = userRepository;
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto) {
        if(workspaceRepository.findByName(workspaceRequestDto.getName()).isPresent()){
            throw new NotFoundWorkspaceException();
        }

        Workspace workspace = new Workspace(workspaceRequestDto.getName(), workspaceRequestDto.getDescription());
        workspaceRepository.save(workspace);

        return new WorkspaceResponseDto(workspace.getName(), workspaceRequestDto.getDescription());
    }


    public EmailResponseDto inviteUserToWorkspace(User user, Long workspaceId, Long userId) {
        // 유저가 관리자인지 체크
        if (!userWorkspaceService.isWorkspaceMember(user.getId(), workspaceId)) {
            throw new BadAccessUserException();
        }
        // 워크 스페이스와 유저 존재 체크
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(NotFoundWorkspaceException::new);
        User newUser = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);

        // 워크 스페이스에 초대
        userWorkspaceService.inviteToWorkspace(newUser, workspace);

        return new EmailResponseDto(newUser.getEmail());
    }

    @Transactional
    public String callYes(User user,Long workspaceId) {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        UserWorkspace userWorkspace = userWorkspaceService.isMember(user.getId(), workspaceId);

        // activation을 true로 전환
        userWorkspace.UserBeActive();

        return "초대 수락 완료";
    }

    @Transactional
    public String callNo(User user,Long workspaceId) {
        // 유저가 중간 테이블에 포함되어 있는지 체크
        UserWorkspace userWorkspace = userWorkspaceService.isMember(user.getId(), workspaceId);

        // 중간 테이블에서 삭제
        userWorkspaceRepository.delete(userWorkspace);

        return "초대 거절 완료";
    }
//
//    public Page<WorkspaceResponseDto> viewOwnWorkspace(User user, Long workspaceid) {
//        // 유저가 관리자인지 체크
//
//        // 페이징으로 출력
//    }
//
//    public UpdateWorkspaceResponseDto updateWorkspace(User user, WorkspaceRequestDto workspaceRequestDto) {
//        // 유저가 관리자인지 체크
//
//        // 수정 하고 저장
//    }
//
//    public DeleteWorkspaceResponseDto deleteWorkspace(User user, DeleteWorkspaceRequestDto deleteWorkspaceRequestDto) {
//        // 유저가 관리자인지 체크
//
//        // 날려
//    }
}
