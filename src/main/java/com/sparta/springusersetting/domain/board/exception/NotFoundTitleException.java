package com.sparta.springusersetting.domain.board.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;

public class NotFoundTitleException extends GlobalException {
    public NotFoundTitleException() { super(GlobalExceptionConst.NOT_FOUND_TITLE);}
}
