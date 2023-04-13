package com.yundou.www.common.defintion.exception;

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix;
import com.yundou.www.common.defintion.domain.response.ErrorData;
import com.yundou.www.common.defintion.domain.response.HttpStatus;
import org.springframework.http.HttpHeaders;

/**
 * 远程未知异常出现了5xx 触发熔断
 */
public class RemoteRuntimeException extends RuntimeException implements ExceptionNotWrappedByHystrix {
    private HttpStatus httpStatus;
    private HttpHeaders headers;
    private ErrorData errorData;

    public RemoteRuntimeException(HttpStatus httpStatus, HttpHeaders headers, ErrorData errorData) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.errorData = errorData;
    }

    public RemoteRuntimeException() {

    }

    public HttpHeaders getHeaders(){
        return headers;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    @Override
    public String toString() {
        return "RemoteRuntimeException{" +
                "httpStatus=" + httpStatus +
                ", headers=" + headers +
                ", errorData=" + errorData +
                "} " + super.toString();
    }
}
