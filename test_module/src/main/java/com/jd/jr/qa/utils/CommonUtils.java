package com.jd.jr.qa.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by Gochin on 2021/2/9.
 */
public class CommonUtils {

    /**
     * @param param sandbox命令传参
     * @param tClass 入参class
     * @param <T> 入参
     * @return 返回
     */
    public <T> T convertMapToBean(final Map<String, String> param, Class<T> tClass) {
        return JSON.parseObject( JSON.toJSONString( param ), tClass );
    }
}
