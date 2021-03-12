package com.jd.jr.qa.entity;

/**
 * Created by Gochin on 2021/2/9.
 */
public class ExceptionFactoryRequestEntity extends BaseRequestEntity {

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    /**
     * 出参返回数值
     */
    public String exceptionType;

}
