package com.jd.jr.qa.entity;

/**
 * Created by Gochin on 2021/2/9.
 */

public class MethodMockRequestEntity extends BaseRequestEntity {
    /**
     * 出参返回数值
     */
    public String responseString;
    /**
     * 出参class的绝对路径
     */
    public String responseEntityClassName;

    /**
     * 根据pin返回mock数据
     */
    public String mockPin;


    public String getMockPin() {
        return mockPin;
    }

    public void setMockPin(String mockPin) {
        this.mockPin = mockPin;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public String getResponseEntityClassName() {
        return responseEntityClassName;
    }

    public void setResponseEntityClassName(String responseEntityClassName) {
        this.responseEntityClassName = responseEntityClassName;
    }
}
