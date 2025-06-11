package com.weiwei.wang.common.exception;

public class BusinessException extends RuntimeException implements ExceptionResponse{

    private Integer code;

    private String message;


    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable cause, Integer code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
