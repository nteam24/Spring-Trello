package com.sparta.springusersetting.domain.userWorkspace.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.ALREADY_ADMIN;


public class BadAccessUserWorkspaceException extends GlobalException {
    public BadAccessUserWorkspaceException() {
        super(ALREADY_ADMIN);
    }
}