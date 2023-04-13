package com.yundou.www.common.defintion.exception;

public class LocalBusinessException extends RuntimeException{

    public Integer businessErrorCode;

    public String businessErrorMessage;

    public Integer getBusinessErrorCode() {
        return businessErrorCode;
    }

    public void setBusinessErrorCode(Integer businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public String getBusinessErrorMessage() {
        return businessErrorMessage;
    }

    public void setBusinessErrorMessage(String businessErrorMessage) {
        this.businessErrorMessage = businessErrorMessage;
    }
}
