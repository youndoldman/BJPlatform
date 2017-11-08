package com.donno.nj.exception.handler;

import com.donno.nj.exception.BaseException;
import com.donno.nj.exception.ClientSideBusinessException;
import com.donno.nj.exception.ForbiddenException;
import com.donno.nj.exception.ServerSideBusinessException;
import com.donno.nj.exception.UnauthorizedException;
import com.donno.nj.representation.Rep;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            UnauthorizedException.class,
            ForbiddenException.class,
            ServerSideBusinessException.class,
            ClientSideBusinessException.class
    })
	public ResponseEntity handleException(BaseException ex){
        logger.error(getExceptionErrorDetails(ex));
        return Rep.error(ex.getHttpCode(), ex.getMessage());
    }


    @ExceptionHandler({Exception.class })
    public ResponseEntity handleNoHandlerFoundException(Exception ex){
        ex.printStackTrace();
//        logger.error(getExceptionErrorDetails(ex));
        return Rep.error(HttpStatus.INTERNAL_SERVER_ERROR, getExceptionErrorDetails(ex));
    }

    @Override
    protected ResponseEntity handleBindException(BindException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorMessage.append(String.format("error on filed: %s, message: %s. ", error.getField(), error.getDefaultMessage()));
        }
        return Rep.error(HttpStatus.BAD_REQUEST, errorMessage.toString());
    }

    @Override
    protected ResponseEntity handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Rep.error(status, e.toString());
    }

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return Rep.error(status, "param error");
    }


    private String getExceptionErrorDetails(Throwable ex) {
        try {
            StringBuilder message = new StringBuilder(ex.getMessage());
            Throwable err = ex;
            while (err.getCause() != null) {
                message.append(err.getCause().getMessage()).append(" : ");
                err = err.getCause();
            }
            return message.toString();
        } catch (Exception e) {
            return "no details error info";
        }
    }
}
