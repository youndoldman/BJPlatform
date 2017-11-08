package com.donno.nj.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        this(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
