package com.yundou.www.common.defintion.domain.response;

public class Result<T> {

    private T data;

    private Integer code;

    private String message;

    private String traceId;

    public static Result<ErrorData> failedResult(ErrorData errorData, int code, String message) {

        Result result = new Result();
        result.setData(errorData);
        result.setTraceId(errorData.getTraceId());
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


    public static Result<ErrorData> failedResult(ErrorData errorData, ResultCode resultCode) {

        Result result = new Result();
        result.setData(errorData);
        result.setTraceId(errorData.getTraceId());
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
