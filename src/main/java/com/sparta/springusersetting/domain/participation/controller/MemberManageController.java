package com.sparta.springusersetting.domain.participation.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberManageController {
    private final MemberManageService memberManageService;

    public MemberManageController(MemberManageService memberManageService) {
        this.memberManageService = memberManageService;
    }


    // 일반 유저를 관리자로 등록하기
    @PostMapping("/workspaces/{workspaceId}/users/{userId}/admin")
    public ResponseEntity<ApiResponse<String>> setWorkspaceAdmin(@PathVariable Long userId, @PathVariable Long workspaceId){
        return ResponseEntity.ok(ApiResponse.success(memberManageService.setWorkspaceAdmin(userId, workspaceId)));

    }

    // 초대 하기
    @PostMapping("/workspaces/{workspaceId}/users/{userId}/invitations")
    public ResponseEntity<ApiResponse<String>> inviteUserToWorkspace(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long workspaceId, @PathVariable Long userId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(memberManageService.inviteUserToWorkspace(user,workspaceId,userId)));
    }

    // 초대 응답
    @PostMapping("/workspaces/{workspaceId}/invitations/accept")
    public ResponseEntity<ApiResponse<String>> callYes(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(memberManageService.callYes(user,workspaceId)));
    }
    @PostMapping("/workspaces/{workspaceId}/invitations/reject")
    public ResponseEntity<ApiResponse<String>> callNo(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(memberManageService.callNo(user,workspaceId)));
    }

    // 소속 맴버 권한 교체
    @PutMapping("/workspaces/{workspaceId}/users/{userId}")
    public ResponseEntity<ApiResponse<String>> changeMemberRole(@AuthenticationPrincipal AuthUser authUser,
                                                                @RequestParam String role,
                                                                @PathVariable Long workspaceId,
                                                                @PathVariable Long userId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(memberManageService.changeMemberRole(user,workspaceId,userId,role)));
    }

}
