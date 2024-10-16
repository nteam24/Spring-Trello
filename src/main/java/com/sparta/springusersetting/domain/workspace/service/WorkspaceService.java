package com.sparta.springusersetting.domain.workspace.service;

import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import com.sparta.springusersetting.domain.workspace.exception.DuplicateWorkspaceException;
import com.sparta.springusersetting.domain.workspace.exception.NotFoundWorkspaceException;
import com.sparta.springusersetting.domain.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto) {
        if(workspaceRepository.findByName(workspaceRequestDto.getName()).isPresent()){
            throw new DuplicateWorkspaceException();
        }

        Workspace workspace = new Workspace(workspaceRequestDto.getName(), workspaceRequestDto.getDescription());
        workspaceRepository.save(workspace);

        return new WorkspaceResponseDto(workspace.getName(), workspaceRequestDto.getDescription());
    }

    // id로 Workspace 반환
    public Workspace findWorkspace(Long workspaceId){
        return workspaceRepository.findById(workspaceId).orElseThrow(NotFoundWorkspaceException::new);
    }

    // 삭제
    public void deleteWorkspace(Workspace workspace){
        workspaceRepository.delete(workspace);
    }

    // cicd 테스트용 주석111111111111111
}
