package com.donno.nj.representation;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Rep {

    public static final String SUCCESS = "success";
    private Boolean success;
    private Object content;
    private Integer errorCode;
    private String message;

    public static ResponseEntity assemble(HttpStatus status) {
        return assemble(status, SUCCESS, null);
    }
    public static  ResponseEntity assemble(HttpStatus status, Object data) {
        return assemble(status, SUCCESS, data);
    }
    public static  ResponseEntity assemble(HttpStatus status, String msg, Object data) {
        Rep rep = new Rep();
        rep.setContent(data);
        rep.setErrorCode(0);
        rep.setMessage(msg);
        rep.setSuccess(true);
        return new ResponseEntity(rep, status);
    }

    public static ResponseEntity<Rep> error(HttpStatus status) {
        return error(status, status.toString());
    }

    public static ResponseEntity<Rep> error(HttpStatus status, String message) {
        Rep rep = new Rep();
        rep.setErrorCode(status.value());
        rep.setMessage(message);
        rep.setSuccess(false);
        return new ResponseEntity(rep, status);
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
