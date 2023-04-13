package com.yundou.www.common.defintion.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.yundou.www.common.defintion.domain.response.ErrorData;
import com.yundou.www.common.defintion.domain.response.HttpStatus;
import org.springframework.http.HttpHeaders;

public class RemoteBadRequestException extends HystrixBadRequestException {
    private HttpStatus httpStatus;
    private HttpHeaders headers;
    private ErrorData errorData;

    public RemoteBadRequestException(HttpStatus httpStatus, HttpHeaders headers, ErrorData errorData) {
        super(errorData.getErrorMessage(), null);
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.errorData = errorData;
    }

    public RemoteBadRequestException() {
        super(null);
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return "RemoteBadRequestException{" +
                "httpStatus=" + httpStatus +
                ", headers=" + headers +
                ", errorData=" + errorData +
                "} " + super.toString();
    }

}
