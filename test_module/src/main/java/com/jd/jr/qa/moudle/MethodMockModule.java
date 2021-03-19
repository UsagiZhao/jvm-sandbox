package com.jd.jr.qa.moudle;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.Map;

import java.util.logging.Logger;

/**
 * Created by Gochin on 2021/2/9.
 */

@MetaInfServices(Module.class)
@Information(id = "mock-module", version = "0.0.2", author = "zhaoguochun@jd.com")
@Slf4j
public class MethodMockModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * @param param 链接？后的参数
     */
    @Command("beforemock")// 模块命令名
    public void beforeMock(final Map<String, String> param) {
        //命令规则 "./sandbox -p xxxxx -d mock/beforemock?className=&methodName=&response=
        //获取参数 注意！！！加载模块时候 命令中的参数名称必须与para中的key保持一致！！！否则会获取不到。
        final String className = param.get( "className" );
        final String methodName = param.get( "methodName" );
        final String response = param.get( "response" );

        new EventWatchBuilder( moduleEventWatcher ).onClass( className ).onBehavior( methodName ).onWatch( new AdviceListener() {
            //对方法执行之前执行
            @Override
            protected void before(Advice advice) throws Throwable {
                String verifyCredentialsNoAndNameV3_result = "";
                log.info( Constants.Sandbox_Default_LogInfo + "调用方法前执行mock，被mock方法 {}#{}", className, methodName );
                //获取方法的所有参数
                String className = advice.getBehavior().getDeclaringClass().getName();
                String methodName = advice.getBehavior().getName();
                if (StringUtils.isNotEmpty( response )) {
                    verifyCredentialsNoAndNameV3_result = response;
                    log.info( Constants.Sandbox_Default_LogInfo + "方法{}#{}调用前返回mock结果:{}", className, methodName, verifyCredentialsNoAndNameV3_result );
                }
                ProcessController.returnImmediately( JSON.parse( verifyCredentialsNoAndNameV3_result ) );
//                log.info( Constants.Sandbox_Default_LogInfo + "方法：" + className + "#" + methodName + "mock完毕！" );
                log.info( Constants.Sandbox_Default_LogInfo + "方法：{}#{},mock完毕！", className, methodName );
            }
        } );
    }

    @Command("afteremock")// 模块命令名
    public void afteremock(final Map<String, String> param) {
        //命令规则 "./sandbox -p xxxxx -d mock/afteremock?className=&methodName=&response=
        //获取参数 注意！！！加载模块时候 命令中的参数名称必须与para中的key保持一致！！！否则会获取不到。
        final String className = param.get( "className" );
        final String methodName = param.get( "methodName" );
        final String response = param.get( "response" );

        new EventWatchBuilder( moduleEventWatcher ).onClass( className ).onBehavior( methodName ).onWatch( new AdviceListener() {
            //对方法执行之前执行
            @Override
            protected void after(Advice advice) throws Throwable {
                String verifyCredentialsNoAndNameV3_result = "";
                log.info( Constants.Sandbox_Default_LogInfo + "方法调用后返回mock数据，被mock方法：" + className + "#" + methodName );
                //获取方法的所有参数
                String className = advice.getBehavior().getDeclaringClass().getName();
                String methodName = advice.getBehavior().getName();
                if (StringUtils.isNotEmpty( response )) {
                    verifyCredentialsNoAndNameV3_result = response;
                    log.info( Constants.Sandbox_Default_LogInfo + "方法{}#{}调用后返回mock结果:{}", className, methodName, verifyCredentialsNoAndNameV3_result );
                }
                ProcessController.returnImmediately( JSON.parse( verifyCredentialsNoAndNameV3_result ) );
                log.info( Constants.Sandbox_Default_LogInfo + "方法：{}#{},mock完毕！", className, methodName );
            }
        } );
    }

}
