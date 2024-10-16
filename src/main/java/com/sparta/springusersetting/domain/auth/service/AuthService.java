package com.sparta.springusersetting.domain.auth.service;

import com.sparta.springusersetting.config.JwtUtil;
import com.sparta.springusersetting.domain.auth.dto.request.SigninRequestDto;
import com.sparta.springusersetting.domain.auth.dto.request.SignupRequestDto;
import com.sparta.springusersetting.domain.auth.dto.response.SigninResponse;
import com.sparta.springusersetting.domain.auth.dto.response.SignupResponse;
import com.sparta.springusersetting.domain.auth.exception.DuplicateEmailException;
import com.sparta.springusersetting.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.springusersetting.domain.notification.util.NotificationUtil;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.notification.discordNotification.service.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final DiscordNotificationService discordNotificationService;
    private final NotificationUtil notificationUtil;

    @Transactional
    public SignupResponse signup(SignupRequestDto signupRequestDto) {

        if (userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserRole userRole = UserRole.of(signupRequestDto.getUserRole());

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getUserName(),
                userRole
        );
        User savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        discordNotificationService.sendDiscordNotification("%s 님이 새로운 회원이 되셨어요 !", newUser.getUserName());

        return new SignupResponse(bearerToken);
    }

    public SigninResponse signin(SigninRequestDto signinRequestDto) throws IOException {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                NotFoundUserException::new);

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        notificationUtil.LoginNotification(user);

        return new SigninResponse(bearerToken);
    }
}
