package com.sparta.springusersetting.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    ROLE_WORKSPACE_ADMIN(WorkSpaceAuthority.WORKSPACE_ADMIN),      // 1. 워크 스페이스 관리자
    ROLE_EDIT_USER(WorkSpaceAuthority.EDIT_USER),    // 2. 편집 가능 권한
    ROLE_READ_USER(WorkSpaceAuthority.READ_USER);// 3. 읽기 권한

    private final String userRole;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserRole"));
    }

    public static class WorkSpaceAuthority {
        public static final String WORKSPACE_ADMIN = "ROLE_READ_USER";
        public static final String EDIT_USER = "ROLE_EDIT_USER";
        public static final String READ_USER ="ROLE_READ_USER";
    }
}