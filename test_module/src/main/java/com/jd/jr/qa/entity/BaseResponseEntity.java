package com.jd.jr.qa.entity;

import com.jd.jr.qa.constants.enums.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gochin on 2021/3/29.
 */
@Getter
@Setter
public class BaseResponseEntity {

    /**
     * 需要mock的类绝对路径
     */
    public String code;
    /**
     * 需要mock的方法
     */
    public boolean success;
    /**
     * 返回消息
     */
    public String msg;

    public BaseResponseEntity(boolean isSuccess){
        if (isSuccess){
            this.code = ResponseEnum.Success.getCode();
            this.success = true;
            this.msg = ResponseEnum.Success.getDesc();
        } else {
            this.code = ResponseEnum.Fail.getCode();
            this.success = false;
            this.msg = ResponseEnum.Fail.getDesc();
        }
    }

    public BaseResponseEntity(){}
}
