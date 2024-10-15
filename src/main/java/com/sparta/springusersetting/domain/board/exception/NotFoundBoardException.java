package com.sparta.springusersetting.domain.board.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;


public class NotFoundBoardException extends GlobalException {
    public NotFoundBoardException() { super(GlobalExceptionConst.NOT_FOUND_BOARD); }

}
