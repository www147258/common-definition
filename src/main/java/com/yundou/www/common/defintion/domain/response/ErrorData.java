package com.yundou.www.common.defintion.domain.response;

public class ErrorData {

    /**
     * 业务的错误响应码
     */
    private String errorCode;

    /**
     * 错误的消息
     */
    private String errorMessage;


    /**
     * 出现问题的服务名称
     * spring.application.name
     */
    private String serviceName;

    /**
     * 出现问题的路径
     */
    private String errorPath;

    /**
     * 异常类名
     */
    private String errorClassName;

    /**
     * 链路跟踪使用的
     */
    private String traceId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }

    public String getErrorClassName() {
        return errorClassName;
    }

    public void setErrorClassName(String errorClassName) {
        this.errorClassName = errorClassName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    @Override
    public String toString() {
        return "ErrorData{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", errorPath='" + errorPath + '\'' +
                ", errorClassName='" + errorClassName + '\'' +
                ", traceId='" + traceId + '\'' +
                '}';
    }
}
