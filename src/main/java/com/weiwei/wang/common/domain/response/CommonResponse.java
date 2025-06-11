package com.weiwei.wang.common.domain.response;

import com.weiwei.wang.common.enums.ResponseCodeAndMessageEnum;
import com.weiwei.wang.common.exception.ExceptionResponse;

public class CommonResponse<T> {

    private Integer code;

    private String message;

    private T data;


    public static CommonResponse success() {

        return success(null);
    }

    public static <T> CommonResponse success(T data) {
        return buildCommonResponse(ResponseCodeAndMessageEnum.SUCCESS, data);
    }

    public static CommonResponse fail(ExceptionResponse exceptionResponse) {

        return buildCommonResponse(exceptionResponse, null);
    }

    private static <T> CommonResponse buildCommonResponse(ExceptionResponse exceptionResponse, T data) {

        CommonResponse response = new CommonResponse();
        response.setCode(exceptionResponse.getCode());
        response.setMessage(exceptionResponse.getMessage());
        response.setData(data);
        return response;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
