package com.sparta.springusersetting.domain.auth.controller;

import com.sparta.springusersetting.config.ApiResponse;
import com.sparta.springusersetting.domain.auth.dto.request.SigninRequestDto;
import com.sparta.springusersetting.domain.auth.dto.request.SignupRequestDto;
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

import java.io.IOException;

@RestController
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(ApiResponse.success(authService.signup(signupRequestDto)));
    }

    // 로그인
    @PostMapping("/auth/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) throws AuthException, IOException{
        return ResponseEntity.ok(ApiResponse.success(authService.signin(signinRequestDto)));
    }
}