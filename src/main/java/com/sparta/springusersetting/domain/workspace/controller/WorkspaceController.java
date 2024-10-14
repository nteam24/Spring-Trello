package com.sparta.springusersetting.domain.workspace.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    // 워크스페이스 생성
    @PostMapping()
    public ResponseEntity<ApiResponse<WorkspaceResponseDto>> createWorkspace( @RequestBody WorkspaceRequestDto workspaceRequestDto){
        return ResponseEntity.ok(ApiResponse.success(workspaceService.createWorkspace(workspaceRequestDto)));
    }

    // 초대 하기

    // 초대 응답

    // 워크 스페이스 조회
    // 워크 스페이스 수정 및 삭제


}
