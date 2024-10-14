package com.sparta.springusersetting.domain.userWorkspace.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.userWorkspace.service.UserWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserWorkspaceController {

    private final UserWorkspaceService userWorkspaceService;

    public UserWorkspaceController(UserWorkspaceService userWorkspaceService) {
        this.userWorkspaceService = userWorkspaceService;
    }

    // 일반 유저를 관리자로 등록하기
    @PostMapping("/admin/user/{userId}/workspace/{workspaceId}")
    public ResponseEntity<ApiResponse<String>> setWorkspaceAdmin(@PathVariable Long userId, @PathVariable Long workspaceId){
        return ResponseEntity.ok(ApiResponse.success(userWorkspaceService.setWorkspaceAdmin(userId, workspaceId)));

    }

}
