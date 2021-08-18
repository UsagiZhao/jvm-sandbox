package com.jd.jr.qa.moudle;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessControlException;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.constants.Constants;
import com.jd.jr.qa.entity.BaseResponseEntity;
import com.jd.jr.qa.entity.MethodMockRequestEntity;
import com.jd.jr.qa.utils.MapUtils;
import com.jd.jr.qa.utils.ValidatorUtil;
import com.jd.jr.qa.utils.validator.MethodMockMoudleValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Gochin on 2021/2/9.
 */

@MetaInfServices(Module.class)
@Information(id = "mock-module", version = "0.0.2", author = "zhaoguochun@jd.com")
@Slf4j
public class MethodMockModule implements Module {
    @Resource
    private ConfigInfo configInfo;
    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    private final MethodMockMoudleValidator methodMockMoudleValidator = new MethodMockMoudleValidator();

    /**
     * @param param 链接？后的参数
     */
    @Command("beforemock")// 模块命令名
    public void beforeMock(final Map<String, String> param) throws Exception {
        //命令规则 "./sandbox -p xxxxx -d mock/beforemock?className=&methodName=&response=
        //获取参数 注意！！！加载模块时候 命令中的参数名称必须与para中的key保持一致！！！否则会获取不到。
        final MethodMockRequestEntity entity = MapUtils.mapToEntity( MethodMockRequestEntity.class, param, "className", "methodName", "responseString", "nameSpace" );
        log.info( "MapToEntity 转换完毕！entity：{}", JSON.toJSONString( entity ) );
        methodMockMoudleValidator.validate( entity );
        if (entity == null) {
            throw new Exception( "param转换entity异常，entity为null" );
        }
        new EventWatchBuilder( moduleEventWatcher ).onClass( entity.getClassName() ).onBehavior( entity.getMethodName() ).onWatch( new AdviceListener() {
            //对方法执行之前执行
            @Override
            protected void before(Advice advice) {
                //判定mock时传的命名空间，只有命名空间一致才可以进行mock
                if (configInfo.getNamespace().equals( entity.getNameSpace() )) {
                    BaseResponseEntity response = mock( advice, entity.getClassName(), entity.getMethodName(), entity.getResponseString() );
                    log.info( Constants.Sandbox_Default_LogInfo + "是否mokc成功：{} , mock完毕！", response.isSuccess() );
                }
            }
        } );
    }

    @Command("afteremock")// 模块命令名
    public void afteremock(final Map<String, String> param) throws Exception {
        //命令规则 "./sandbox -p xxxxx -d mock/afteremock?className=&methodName=&response=
        //获取参数 注意！！！加载模块时候 命令中的参数名称必须与para中的key保持一致！！！否则会获取不到。
        final String cnPattern = StringUtils.isEmpty( param.get( "className" ) ) ? "" : param.get( "className" );
        final String mnPattern = StringUtils.isEmpty( param.get( "methodName" ) ) ? "" : param.get( "methodName" );
        final String mockString = StringUtils.isEmpty( param.get( "responseString" ) ) ? "" : param.get( "responseString" );
        ValidatorUtil.isAnyEmpty( cnPattern, mnPattern, mockString );

        new EventWatchBuilder( moduleEventWatcher ).onClass( cnPattern ).onBehavior( mnPattern ).onWatch( new AdviceListener() {
            //对方法执行之前执行
            @Override
            protected void after(Advice advice) {
                BaseResponseEntity response = mock( advice, cnPattern, mnPattern, mockString );
                log.info( Constants.Sandbox_Default_LogInfo + "是否mokc成功：{} , mock完毕！", response.isSuccess() );
            }
        } );
    }

    /**
     * @param advice     沙箱通知
     * @param cnPattern  mock类名
     * @param mnPattern  mock的方法名
     * @param mockString mock返回参数
     * @return 请求结果
     */
    public BaseResponseEntity mock(Advice advice, String cnPattern, String mnPattern, String mockString) {

        String returnStr = "";
        log.info( Constants.Sandbox_Default_LogInfo + "调用方法前执行mock，被mock方法 {}#{}", cnPattern, mnPattern );
        //获取方法的所有参数
        String className = advice.getBehavior().getDeclaringClass().getName();
        String methodName = advice.getBehavior().getName();
        Class responseClazz = null;
        //匹配被mock的方法返回类型
        try {
            for (Method method : Class.forName( className ).getMethods()) {
                if (method.getName().equals( methodName )) {
                    responseClazz = method.getReturnType();
                }
            }
            if (ObjectUtils.isNotEmpty( responseClazz ) && StringUtils.isNotEmpty( mockString )) {
                returnStr = mockString;
                log.info( Constants.Sandbox_Default_LogInfo + "方法{}#{}调用后直接返回mock结果:{}", className, methodName, returnStr );
                ProcessController.returnImmediately( JSON.parseObject( returnStr, responseClazz ) );
            }
        } catch (ClassNotFoundException e) {
            log.info( Constants.Sandbox_Default_LogInfo + "获取method失败！！请确认method是否正确" );
            //获取被mock方法的method时失败，无法获取方法的返回参数，mock失败
            return new BaseResponseEntity( false );
        } catch (ProcessControlException e) {
            //mock成功会抛出ProcessControlException，返回mock成功
            return new BaseResponseEntity( true );
        }
        return new BaseResponseEntity( false );

    }

    public boolean checkPinIsNeedMock(Advice advice, Method mockMethod) {
        mockMethod.getParameterTypes();
        advice.getParameterArray();
        return false;
    }
}
