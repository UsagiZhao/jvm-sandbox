package com.jd.jr.qa.utils;

import com.jd.jr.qa.entity.BaseRequestEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Gochin on 2021/7/26.
 */
public class MethodUtils {


    /**循环查询父类进行调用
     * @param request     入参
     * @param childClass  入参class
     * @param methodName  调用的方法名称
     * @param methodValue 入参
     */
    public static void checkSuperClassAndSetValue(Object request, Class<?> childClass, String methodName, String methodValue) {
        Class clazz = childClass.getSuperclass();
        //判断是不是Object。不是Obeject说明还需要继续遍历当前类的父类
        try {
            if (!clazz.getName().equals( Object.class.getName() )) {
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (method.getName().toLowerCase().equals( "set" + methodName.toLowerCase() )) {
                        method.invoke( request, methodValue );
                    }
                }
                checkSuperClassAndSetValue( request, clazz, methodName, methodValue );
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**通过key进行过滤
     * @param methods
     * @param key
     * @return
     */
    public static List<Method> filterMethodByKey(Method[] methods, String key) {
        return Arrays.stream( methods ).filter( method -> method.getName().toLowerCase().contains( key ) ).collect( Collectors.toList() );
    }

    /**通过key进行过滤
     * @param methods
     * @param key
     * @return
     */
    public static List<Method> getFiled(Method[] methods, String key) {
        return Arrays.stream( methods ).filter( method -> method.getName().toLowerCase().contains( key ) ).collect( Collectors.toList() );
    }
}
