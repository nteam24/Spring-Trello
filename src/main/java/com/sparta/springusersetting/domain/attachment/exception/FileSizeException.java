package com.sparta.springusersetting.domain.attachment.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.FILE_SIZE_EXCEEDED;

public class FileSizeException extends GlobalException {
    public FileSizeException() {super(FILE_SIZE_EXCEEDED);}
}
