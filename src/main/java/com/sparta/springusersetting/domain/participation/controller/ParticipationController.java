package com.sparta.springusersetting.domain.participation.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.participation.service.ParticipationService;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.DeleteWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.EmailResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParticipationController {

    private final ParticipationService participationService;

    public ParticipationController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    // 일반 유저를 관리자로 등록하기
    @PostMapping("/workspaces/{workspaceId}/users/{userId}/admin")
    public ResponseEntity<ApiResponse<String>> setWorkspaceAdmin(@PathVariable Long userId, @PathVariable Long workspaceId){
        return ResponseEntity.ok(ApiResponse.success(participationService.setWorkspaceAdmin(userId, workspaceId)));

    }

    // 초대 하기
    @PostMapping("/workspaces/{workspaceId}/users/{userId}/invitations")
    public ResponseEntity<ApiResponse<EmailResponseDto>> inviteUserToWorkspace(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long workspaceId, @PathVariable Long userId){
        User user = User.fromAuthUser(authUser);
        System.out.println("1");
        return ResponseEntity.ok(ApiResponse.success(participationService.inviteUserToWorkspace(user,workspaceId,userId)));
    }

    // 초대 응답
    @PostMapping("/workspaces/{workspaceId}/invitations/accept")
    public ResponseEntity<ApiResponse<String>> callYes(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(participationService.callYes(user,workspaceId)));
    }
    @PostMapping("/workspaces/{workspaceId}/invitations/reject")
    public ResponseEntity<ApiResponse<String>> callNo(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(participationService.callNo(user,workspaceId)));
    }

    // 워크 스페이스 조회
    @GetMapping("/users/me/workspaces")
    public ResponseEntity<ApiResponse<Page<WorkspaceResponseDto>>> viewOwnWorkspace(@RequestParam(defaultValue = "1") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @AuthenticationPrincipal AuthUser authUser
    ){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(participationService.viewOwnWorkspace(page,size,user)));
    }

    // 워크 스페이스 수정
    @PutMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<UpdateWorkspaceResponseDto>> updateWorkspace(@AuthenticationPrincipal AuthUser authUser, @RequestBody WorkspaceRequestDto workspaceRequestDto, @PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(participationService.updateWorkspace(user,workspaceRequestDto,workspaceId)));
    }

    // 워크 스페이스 삭제
    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<ApiResponse<DeleteWorkspaceResponseDto>> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(participationService.deleteWorkspace(user,workspaceId)));
    }
}
