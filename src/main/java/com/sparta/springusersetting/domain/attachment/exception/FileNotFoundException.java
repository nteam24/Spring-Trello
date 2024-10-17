package com.sparta.springusersetting.domain.attachment.exception;
import com.sparta.springusersetting.domain.common.exception.GlobalException;
import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_FILE;
public class FileNotFoundException extends GlobalException {
    public FileNotFoundException() {super(NOT_FOUND_FILE);}
}
