package com.sparta.springusersetting.domain.board.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_BOARD;
public class NotFoundBoardException extends GlobalException {
    public NotFoundBoardException() {
        super(NOT_FOUND_BOARD);
    }
}
