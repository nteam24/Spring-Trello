package com.sparta.springusersetting.domain.participation.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.participation.service.WorkspaceManageService;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class WorkspaceManageController {

    private final WorkspaceManageService workspaceManageService;

    public WorkspaceManageController(WorkspaceManageService workspaceManageService) {
        this.workspaceManageService = workspaceManageService;
    }

    // 워크 스페이스 조회
    @GetMapping("/users/me/workspaces")
    public ResponseEntity<ApiResponse<Page<WorkspaceResponseDto>>> viewOwnWorkspace(@RequestParam(defaultValue = "1") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @AuthenticationPrincipal AuthUser authUser
    ) {
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceManageService.viewOwnWorkspace(page, size, user)));
    }

    // 워크 스페이스 수정
    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<UpdateWorkspaceResponseDto>> updateWorkspace(@AuthenticationPrincipal AuthUser authUser, @RequestBody WorkspaceRequestDto workspaceRequestDto, @PathVariable Long workspaceId) {
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceManageService.updateWorkspace(user, workspaceRequestDto, workspaceId)));
    }

    // 워크 스페이스 삭제
    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<DeleteWorkspaceResponseDto>> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long workspaceId) {
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceManageService.deleteWorkspace(user, workspaceId)));
    }


}