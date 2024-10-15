package com.sparta.springusersetting.domain.card.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.WORKSPACE_ID_REQUIRED;

public class BadAccessCardException extends GlobalException {
    public BadAccessCardException() {
        super(WORKSPACE_ID_REQUIRED);
    }
}
