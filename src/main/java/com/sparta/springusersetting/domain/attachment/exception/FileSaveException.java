package com.sparta.springusersetting.domain.attachment.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.FILE_SAVE_FAILURE;


public class FileSaveException extends GlobalException {
    public FileSaveException() {super(FILE_SAVE_FAILURE);}
}
