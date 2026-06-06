package com.weiwei.wang.common.enums;

import com.weiwei.wang.common.exception.ExceptionResponse;

public enum ResponseCodeAndMessageEnum implements ExceptionResponse {

    SUCCESS(0, "操作成功"),

    FAIL(-1, "操作失败"),

    PARAM_ERROR(400, "请求参数错误"),

    UNAUTHORIZED(401, "未认证"),

    FORBIDDEN(403, "无权限"),

    NOT_FOUND(404, "资源不存在"),

    SYSTEM_ERROR(500, "系统异常"),

    ;

    private final Integer code;

    private final String message;

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
