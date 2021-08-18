package com.jd.jr.qa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gochin on 2021/2/9.
 */
@Setter
@Getter
public class ExceptionFactoryRequestEntity extends BaseRequestEntity {
    /**
     * 出参返回数值
     */
    public String exceptionType;
    /**
     *异常文案
     */
    public String message;

}
