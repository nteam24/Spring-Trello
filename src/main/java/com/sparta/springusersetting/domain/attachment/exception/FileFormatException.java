package com.sparta.springusersetting.domain.attachment.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.INVALID_FILE_FORMAT;

public class FileFormatException extends GlobalException {
    public FileFormatException() {super(INVALID_FILE_FORMAT);}
}
