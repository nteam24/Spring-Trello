package com.sparta.springusersetting.domain.workspace.service;

import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto) {
        Workspace workspace = new Workspace(workspaceRequestDto.getName(), workspaceRequestDto.getDescription());
        workspaceRepository.save(workspace);

        return new WorkspaceResponseDto(workspace.getName(), workspaceRequestDto.getDescription());
    }



}
