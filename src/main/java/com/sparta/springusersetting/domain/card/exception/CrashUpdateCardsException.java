package com.sparta.springusersetting.domain.card.exception;

import com.sparta.springusersetting.domain.common.exception.GlobalException;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;

public class CrashUpdateCardsException extends GlobalException {
    public CrashUpdateCardsException() { super(GlobalExceptionConst.CRASH_UPDATE_CARDS);}
}
