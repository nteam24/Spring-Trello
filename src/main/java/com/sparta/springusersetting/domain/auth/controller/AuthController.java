package com.sparta.springusersetting.domain.auth.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.auth.dto.request.SigninRequest;
import com.sparta.springusersetting.domain.auth.dto.request.SignupRequest;
import com.sparta.springusersetting.domain.auth.dto.response.SigninResponse;
import com.sparta.springusersetting.domain.auth.dto.response.SignupResponse;
import com.sparta.springusersetting.domain.auth.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(ApiResponse.success(authService.signup(signupRequest)));
    }

    // 로그인
    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> signin(@Valid @RequestBody SigninRequest signinRequest) throws AuthException {
        return ResponseEntity.ok(ApiResponse.success(authService.signin(signinRequest)));
    }
}