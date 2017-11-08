package com.donno.nj.exception;

import org.springframework.http.HttpStatus;

public class BaseException  extends RuntimeException{

    private HttpStatus httpStatus;

    private BaseException(String message) {
        super(message);
    }

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpCode() {
        return httpStatus;
    }
}
