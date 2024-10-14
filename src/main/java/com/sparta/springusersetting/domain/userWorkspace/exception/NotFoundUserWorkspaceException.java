package com.sparta.springusersetting.domain.userWorkspace.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;


import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_USER_WORKSPACE;

public class NotFoundUserWorkspaceException extends GlobalException {
    public NotFoundUserWorkspaceException() {
        super(NOT_FOUND_USER_WORKSPACE);
    }
}
