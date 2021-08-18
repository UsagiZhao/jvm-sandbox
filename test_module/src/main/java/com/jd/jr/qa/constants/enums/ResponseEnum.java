package com.jd.jr.qa.constants.enums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * Created by Gochin on 2021/3/29.
 */
public enum ResponseEnum {

    Success( "00000", "Success", true ),
    Fail( "99999", "Failed", false ),
    //参数校验类异常code从10000开始
    ARG_IS_NULL_ERROR( "10001", "Any parameter can not be empty", false ),
    //业务异常类code从20000开始
    PIN_NOT_NEED_MOCK( "20001", "Pin without mock！", false );


    private boolean success = false;
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    ResponseEnum(String code, String desc, boolean success) {
        this.code = code;
        this.desc = desc;
        this.success = success;
    }


    public boolean isSuccess() {
        return this.success;
    }

    public String toJson() {
        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(ResponseEnum.class);
        return JSON.toJSONString( this,config );
    }
}
