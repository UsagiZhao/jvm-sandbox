package com.jd.jr.qa.entity;

/**
 * Created by Gochin on 2021/2/9.
 */
public class BaseRequestEntity {
    /**
     * 需要mock的类绝对路径
     */
    public String className;
    /**
     * 需要mock的方法
     */
    public String methodName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

}
