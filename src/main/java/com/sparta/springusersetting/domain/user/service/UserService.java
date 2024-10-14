package com.sparta.springusersetting.domain.user.service;

import com.sparta.springusersetting.domain.auth.exception.InvalidPasswordFormatException;
import com.sparta.springusersetting.domain.user.dto.request.UserChangePasswordRequest;
import com.sparta.springusersetting.domain.user.dto.response.UserResponse;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.exception.DuplicatePasswordException;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.auth.exception.UnauthorizedPasswordException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 조회 ( id )
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        return new UserResponse(user.getId(), user.getEmail());
    }

    // 유저 비밀번호 변경
    @Transactional
    public String changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        validateNewPassword(userChangePasswordRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new DuplicatePasswordException();
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new UnauthorizedPasswordException();
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));

        return "비밀번호가 정상적으로 변경되었습니다.";
    }

    // 유저 회원탈퇴
    @Transactional
    public String deleteUser(long userId) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        // UserStatus DELETED 로 수정
        user.delete();

        return "회원탈퇴가 정상적으로 완료되었습니다.";
    }


    // 패스워드 조건 확인 메서드
    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidPasswordFormatException();
        }
    }
}

