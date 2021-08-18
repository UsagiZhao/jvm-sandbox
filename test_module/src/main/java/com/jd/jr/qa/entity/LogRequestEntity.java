package com.jd.jr.qa.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gochin on 2021/2/9.
 */

@Setter
@Getter
public class LogRequestEntity extends BaseRequestEntity {
    /**
     * 代码执行行
     */
    public String lineNumber;


}
