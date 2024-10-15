package com.sparta.springusersetting.domain.board.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;

public class UnauthorizedActionException extends GlobalException {
    public UnauthorizedActionException() { super(GlobalExceptionConst.UNAUTHORIZED_READONLY);
    }
}
