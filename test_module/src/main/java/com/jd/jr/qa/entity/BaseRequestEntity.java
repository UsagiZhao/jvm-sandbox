package com.jd.jr.qa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gochin on 2021/2/9.
 */
@Getter
@Setter
public class BaseRequestEntity {
    /**
     * 需要mock的类绝对路径
     */
    public String className;
    /**
     * 需要mock的方法
     */
    public String methodName;
    /**
     * 命名空间
     */
    public String nameSpace;

}
