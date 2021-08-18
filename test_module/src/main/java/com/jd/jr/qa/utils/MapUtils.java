package com.jd.jr.qa.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.jd.jr.qa.entity.MethodMockRequestEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Gochin on 2021/7/26.
 */
@Slf4j
public class MapUtils {

    /**
     * @param clazz           入参class
     * @param param           沙箱传过来的map
     * @param doSetMethodName 调用setXXX的方法名字
     * @param <T>             出参
     * @return
     */
    public static <T> T mapToEntity(Class<T> clazz, Map<String, String> param, String... doSetMethodName) {
        AtomicBoolean isInvoke = new AtomicBoolean( false );
        try {
            T request = clazz.newInstance();
            Method[] methods = request.getClass().getMethods();
            //过滤method
            List<Method> ms = MethodUtils.filterMethodByKey( methods, "set" );
            Arrays.stream( doSetMethodName ).forEach( s -> {
                try {
                    for (Method m : ms) {
                        if (m.getName().toLowerCase().equals( "set" + s.toLowerCase() )) {
                            m.invoke( request, param.get( s ) );
                            isInvoke.set( true );
                            break;
                        }
                    }
//                    if (!Boolean.parseBoolean( isInvoke.toString() )) {
//                        methodUtils.checkSuperClassAndSetValue( request, clazz, s, param.get( s ) );
//                    }
//                    isInvoke.set( false );
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            } );
            return request;
        } catch (IllegalAccessException | InstantiationException e) {
            log.error( "mapToEntity 执行失败;" );
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        Map param = Maps.newHashMap();
        param.put( "methodName", "fjd" );
        param.put( "className", "123" );
        param.put( "responseString", "response" );
        param.put( "nameSpace", "nameSpace" );
        MapUtils mapUtils = new MapUtils();
        MethodMockRequestEntity requestEntity = (MethodMockRequestEntity) mapUtils.mapToEntity( MethodMockRequestEntity.class, param, "methodName", "className", "responseString", "nameSpace" );
        System.out.println( JSON.toJSONString( requestEntity ) );
    }
}
