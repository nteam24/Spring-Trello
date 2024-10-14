package com.sparta.springusersetting.domain.workspace.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.dto.request.WorkspaceRequestDto;
import com.sparta.springusersetting.domain.workspace.dto.response.EmailResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.UpdateWorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.dto.response.WorkspaceResponseDto;
import com.sparta.springusersetting.domain.workspace.service.WorkspaceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces")
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
    @PostMapping("/{workspaceId}/{userId}")
    public ResponseEntity<ApiResponse<EmailResponseDto>> inviteUserToWorkspace(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId, @PathVariable Long userId){
        User user = User.fromAuthUser(authUser);
        System.out.println("1");
        return ResponseEntity.ok(ApiResponse.success(workspaceService.inviteUserToWorkspace(user,workspaceId,userId)));
    }

    // 초대 응답
    @PostMapping("/invite/{workspaceId}/yes")
    public ResponseEntity<ApiResponse<String>> callYes(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceService.callYes(user,workspaceId)));
    }
    @PostMapping("/invite/{workspaceId}/no")
    public ResponseEntity<ApiResponse<String>> callNo(@AuthenticationPrincipal AuthUser authUser,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceService.callNo(user,workspaceId)));
    }

    // 워크 스페이스 조회
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<WorkspaceResponseDto>>> viewOwnWorkspace(@RequestParam(defaultValue = "1") int page,
                                                                                    @RequestParam(defaultValue = "10") int size,
                                                                                    @AuthenticationPrincipal AuthUser authUser
                                                                                    ){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceService.viewOwnWorkspace(page,size,user)));
    }
    // 워크 스페이스 수정
    @PutMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<UpdateWorkspaceResponseDto>> updateWorkspace(@AuthenticationPrincipal AuthUser authUser, @RequestBody WorkspaceRequestDto workspaceRequestDto,@PathVariable Long workspaceId){
        User user = User.fromAuthUser(authUser);
        return ResponseEntity.ok(ApiResponse.success(workspaceService.updateWorkspace(user,workspaceRequestDto,workspaceId)));
    }
//
//    // 워크 스페이스 삭제
//    @DeleteMapping("/workspace/{workspaceId}")
//    public ResponseEntity<ApiResponse<DeleteWorkspaceResponseDto>> deleteWorkspace(@AuthenticationPrincipal AuthUser authUser,@RequestBody DeleteWorkspaceRequestDto deleteWorkspaceRequestDto){
//        User user = User.fromAuthUser(authUser);
//        return ResponseEntity.ok(ApiResponse.success(workspaceService.deleteWorkspace(user,deleteWorkspaceRequestDto)));
//    }


}
