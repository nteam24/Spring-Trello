package com.sparta.springusersetting.domain.card.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;

import static com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst.NOT_FOUND_CARD;

public class NotFoundCardException extends GlobalException {
    public NotFoundCardException() {super(NOT_FOUND_CARD);}
}
