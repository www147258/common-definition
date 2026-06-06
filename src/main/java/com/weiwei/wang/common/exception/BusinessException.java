package com.weiwei.wang.common.exception;

public class BusinessException extends RuntimeException implements ExceptionResponse {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;


    public BusinessException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.getMessage());
        this.code = exceptionResponse.getCode();
        this.message = exceptionResponse.getMessage();
    }


    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable cause, Integer code, String message) {
        super(message, cause);
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
