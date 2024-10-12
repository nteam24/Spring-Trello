package com.sparta.springusersetting.domain.common.dto;

import com.sparta.springusersetting.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final String userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String userId, String email, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}