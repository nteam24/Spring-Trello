package com.sparta.springusersetting.domain.workspace.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    // 워크스페이스 생성
    @PostMapping("/workspaces")
    public ResponseEntity<ApiResponse<WorkspaceResponseDto>> createWorkspace( @RequestBody WorkspaceRequestDto workspaceRequestDto){
        return ResponseEntity.ok(ApiResponse.success(workspaceService.createWorkspace(workspaceRequestDto)));
    }

}
