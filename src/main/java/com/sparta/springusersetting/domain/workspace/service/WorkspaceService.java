package com.sparta.springusersetting.domain.workspace.service;

import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }


//    public WorkspaceRequestDto createWorkspace(User user) {
//        // 사용자가 Admin 인가?
//
//        //
//
//    }
}
