package com.jd.jr.qa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gochin on 2021/2/9.
 */

@Setter
@Getter
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

}
