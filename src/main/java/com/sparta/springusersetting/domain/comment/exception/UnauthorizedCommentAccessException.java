package com.sparta.springusersetting.domain.comment.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_USER_OF_COMMENT;

public class UnauthorizedCommentAccessException extends GlobalException {
    public UnauthorizedCommentAccessException() {
        super(NOT_USER_OF_COMMENT);
    }
}