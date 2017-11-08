package com.donno.nj.exception;

import org.springframework.http.HttpStatus;

public class ServerSideBusinessException extends BaseException {

    public ServerSideBusinessException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ServerSideBusinessException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
