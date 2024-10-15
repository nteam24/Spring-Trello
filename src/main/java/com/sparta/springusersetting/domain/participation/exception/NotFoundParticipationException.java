package com.sparta.springusersetting.domain.participation.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;


import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER_WORKSPACE;

public class NotFoundParticipationException extends GlobalException {
    public NotFoundParticipationException() {
        super(NOT_FOUND_USER_WORKSPACE);
    }
}
