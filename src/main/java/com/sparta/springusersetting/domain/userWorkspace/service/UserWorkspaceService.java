package com.sparta.springusersetting.domain.userWorkspace.service;

import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.userWorkspace.repository.UserWorkspaceRepository;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

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



}
