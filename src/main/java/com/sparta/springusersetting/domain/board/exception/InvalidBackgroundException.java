package com.sparta.springusersetting.domain.board.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;

public class InvalidBackgroundException extends GlobalException {
    public InvalidBackgroundException() { super(GlobalExceptionConst.INVALID_BACKGROUND_COLOR_OR_IMAGE); }
}
