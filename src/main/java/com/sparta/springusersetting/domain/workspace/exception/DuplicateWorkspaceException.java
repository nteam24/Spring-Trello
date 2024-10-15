package com.sparta.springusersetting.domain.workspace.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.DUPLICATE_WORKSPACE_NAME;

public class DuplicateWorkspaceException extends GlobalException {
    public DuplicateWorkspaceException() {
        super(DUPLICATE_WORKSPACE_NAME);
    }
}
