package com.sparta.springusersetting.domain.userWorkspace.service;

import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import com.sparta.springusersetting.domain.userWorkspace.exception.BadAccessUserWorkspaceException;
import com.sparta.springusersetting.domain.userWorkspace.exception.NotFoundUserWorkspaceException;
import com.sparta.springusersetting.domain.userWorkspace.repository.UserWorkspaceRepository;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.springusersetting.domain.user.enums.MemberRole.ROLE_WORKSPACE_ADMIN;
import static com.sparta.springusersetting.domain.user.enums.UserRole.ROLE_ADMIN;
import static com.sparta.springusersetting.domain.user.enums.UserRole.ROLE_USER;

@Service
public class UserWorkspaceService {

    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public UserWorkspaceService(UserWorkspaceRepository userWorkspaceRepository, UserRepository userRepository, WorkspaceRepository workspaceRepository) {
        this.userWorkspaceRepository = userWorkspaceRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public String setWorkspaceAdmin(Long userId, Long workspaceId) {

        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        if(user.getUserRole()==ROLE_ADMIN){
            throw new BadAccessUserException();
        }
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(NotFoundWorkspaceException::new);

        // 찾고 없으면 생성
        UserWorkspace userWorkspace = userWorkspaceRepository.findByUserIdAndWorkspaceId(user.getId(), workspace.getId())
                .orElseGet(() -> new UserWorkspace(user, workspace));

        // 관리자 권한 부여
        if(userWorkspace.getMemberRole()==ROLE_WORKSPACE_ADMIN){
            throw new BadAccessUserWorkspaceException();
        }
        userWorkspace.UserBeADMIN();

        // 활성화
        if(!userWorkspace.isActivation()){
            userWorkspace.UserBeActive();
        }
        userWorkspaceRepository.save(userWorkspace);

        return "관리자 설정 완료";
    }
}
