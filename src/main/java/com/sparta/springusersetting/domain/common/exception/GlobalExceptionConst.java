package com.sparta.springusersetting.domain.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionConst {

    // 상태코드 400
    DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, " 새 비밀번호는 이전에 사용한 비밀번호와 같을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, " 새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, " 올바른 권한이 아닙니다."),
    ALREADY_ADMIN(HttpStatus.BAD_REQUEST, " 이미 해당 워크스페이스의 관리자 입니다."),
    WORKSPACE_ID_REQUIRED(HttpStatus.BAD_REQUEST, "워크스페이스 ID가 검색에 필요합니다."),
    INVALID_BACKGROUND_COLOR_OR_IMAGE(HttpStatus.BAD_REQUEST,"배경색 또는 배경이미지 중 하나는 선택해야 합니다."),
    INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST,"파일 형식이 잘못되었습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 크기가 잘못되었습니다."),

    // 상태코드 401
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, " 비밀번호를 확인해주세요."),
    UNAUTHORIZED_READONLY(HttpStatus.UNAUTHORIZED, " 읽기 전용 멤버는 생성할 수 없습니다."),

    // 상태코드 403
    NOT_USER_OF_COMMENT(HttpStatus.FORBIDDEN, " 댓글 작성자가 아닙니다."),
    UNAUTHORIZED_LIST_CREATION(HttpStatus.FORBIDDEN, " 리스트를 생성할 권한이 없습니다."),
    FILE_SAVE_FAILURE(HttpStatus.FORBIDDEN, "파일 저장중 오류가 발생하였습니다."),

    // 상태코드 404git
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, " 회원이 존재하지 않습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, " 이메일을 확인해주세요."),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, " 존재하지 않는 댓글입니다."),
    NOT_FOUND_WORKSPACE(HttpStatus.NOT_FOUND, " 워크스페이스가 존재하지 않습니다."),
    NOT_FOUND_USER_WORKSPACE(HttpStatus.NOT_FOUND, " 해당 유저는 해당 워크스페이스 소속이 아닙니다."),
    NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다."),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND, " 해당 보드는 존재하지 않습니다."),
    NOT_FOUND_LISTS(HttpStatus.NOT_FOUND, " 해당 리스트는 존재하지 않습니다."),
    NOT_FOUND_TITLE(HttpStatus.NOT_FOUND, " 제목이 없습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, " 파일이 없습니다."),
    DELETED_USER(HttpStatus.NOT_FOUND, " 탈퇴된 회원입니다."),

    // 상태코드 409
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, " 중복된 이메일입니다."),
    DUPLICATE_WORKSPACE_NAME(HttpStatus.CONFLICT, " 중복된 워크스페이스명 입니다.");



    private final HttpStatus httpStatus;
    private final String message;
}
