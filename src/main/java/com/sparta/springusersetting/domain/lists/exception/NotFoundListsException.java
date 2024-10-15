package com.sparta.springusersetting.domain.lists.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_LISTS;
public class NotFoundListsException extends GlobalException {
    public NotFoundListsException() {
        super(NOT_FOUND_LISTS);
    }
}
