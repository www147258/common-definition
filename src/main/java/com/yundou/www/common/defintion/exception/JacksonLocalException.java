package com.yundou.www.common.defintion.exception;

public class JacksonLocalException extends RuntimeException{

    public JacksonLocalException() {
    }

    public JacksonLocalException(String message) {
        super(message);
    }

    public JacksonLocalException(String message, Throwable cause) {
        super(message, cause);
    }

    public JacksonLocalException(Throwable cause) {
        super(cause);
    }

    public JacksonLocalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
