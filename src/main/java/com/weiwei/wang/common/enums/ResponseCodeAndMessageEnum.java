package com.weiwei.wang.common.enums;

import com.weiwei.wang.common.exception.ExceptionResponse;

public enum ResponseCodeAndMessageEnum implements ExceptionResponse {

    SUCCESS(0,"操作成功"),


    ;

    private Integer code;

    private String message;


    ResponseCodeAndMessageEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
