package com.sparta.springusersetting.domain.lists.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.UNAUTHORIZED_LIST_CREATION;
public class BadAccessListsException extends GlobalException {
    public BadAccessListsException() {
        super(UNAUTHORIZED_LIST_CREATION);
    }
}
