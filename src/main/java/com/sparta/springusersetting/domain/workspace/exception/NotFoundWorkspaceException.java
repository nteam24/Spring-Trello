package com.sparta.springusersetting.domain.workspace.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_WORKSPACE;

public class NotFoundWorkspaceException extends GlobalException {
    public NotFoundWorkspaceException() {
        super(NOT_FOUND_WORKSPACE);
    }
}
