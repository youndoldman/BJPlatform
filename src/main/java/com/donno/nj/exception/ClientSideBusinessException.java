package com.donno.nj.exception;

import org.springframework.http.HttpStatus;

public class ClientSideBusinessException extends BaseException {

    public ClientSideBusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public ClientSideBusinessException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
