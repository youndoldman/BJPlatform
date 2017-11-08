package com.donno.nj.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        this(message, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
