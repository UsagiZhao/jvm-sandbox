package com.jd.jr.qa.constants.exception;

import com.jd.jr.qa.constants.enums.ResponseEnum;

/**
 * Created by Gochin on 2021/7/26.
 */
public class CheckException extends Exception {

    private boolean success;
    private String responseCode;
    private String responseMessage;


    public CheckException() {
    }

    public CheckException(boolean success, String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    public CheckException(ResponseEnum responseEnum){
        setSuccess(responseEnum.isSuccess());
        setResponseCode(responseEnum.getCode());
        setResponseMessage(responseEnum.getDesc());
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    /**
     * 添加返回值描述。
     * @param responseEnum
     * @param message
     */
    public CheckException(ResponseEnum responseEnum,String message){
        setSuccess(responseEnum.isSuccess());
        setResponseCode(responseEnum.getCode());
        setResponseMessage(message);
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
