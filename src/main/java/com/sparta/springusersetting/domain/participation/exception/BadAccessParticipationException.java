package com.sparta.springusersetting.domain.participation.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.ALREADY_ADMIN;


public class BadAccessParticipationException extends GlobalException {
    public BadAccessParticipationException() {
        super(ALREADY_ADMIN);
    }
}