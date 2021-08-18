package com.jd.jr.qa.moudle;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.constants.Constants;
import com.jd.jr.qa.entity.LogRequestEntity;
import com.jd.jr.qa.entity.MethodMockRequestEntity;
import com.jd.jr.qa.utils.MapUtils;
import com.jd.jr.qa.utils.validator.LogMoudleValidator;
import com.jd.jr.qa.utils.validator.MethodMockMoudleValidator;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by Gochin on 2021/2/8.
 */

@MetaInfServices(Module.class)
@Information(id = "log-module", version = "0.0.2", author = "zhaoguochun@jd.com")
@Slf4j
public class LogModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    private final LogMoudleValidator logMoudleValidator = new LogMoudleValidator();

    /**
     * 添加日志
     *
     * @param param sandbox 命令带参
     */
    //  -d 'log-module/addlog?className=XXX&methodName=XXX'
    @Command("addlog")
    public void addlog(final Map<String, String> param) throws Exception {
        // --- 解析参数 ---
//        final String entity.getClassName() = param.get( "className" );
//        final String entity.getMethodName() = param.get( "methodName" );
//        final String lineNum = param.get( "lineNumber" );
        final LogRequestEntity entity = MapUtils.mapToEntity( LogRequestEntity.class, param, "className", "methodName","lineNumber" );
        log.info( "MapToEntity 转换完毕！entity：{}", JSON.toJSONString( entity ) );
        logMoudleValidator.validate( entity );

        // --- 开始增强 ---
        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher )
                .onClass( entity.getClassName() )
                .onBehavior( entity.getMethodName() )
                .onWatch( new AdviceListener() {
            @Override
            public void before(Advice advice) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start -----before" );
                Object[] parameters = advice.getParameterArray();
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                for (int i = 0; i < parameters.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参：" ).append( JSON.toJSONString( parameters[i] ) ).append( System.getProperty( "line.separator" ) );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，入参————{}", threadId, threadName, entity.getClassName(), entity.getMethodName(), parameterStr );
            }

            @Override
            public void after(Advice advice) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start -----after" );
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                Object[] reqObj = advice.getParameterArray();
                for (int i = 0; i < reqObj.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参：" ).append( JSON.toJSONString( reqObj[i] ) ).append( System.getProperty( "line.separator" ) );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，入参————{}", threadId, threadName, entity.getClassName(), entity.getMethodName(), parameterStr.toString() );
                Object object = advice.getReturnObj();
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{},请求方法：{}#{}，接口返回：{}", threadId, threadName, entity.getClassName(), entity.getMethodName(), JSON.toJSONString( object ) );
            }

            @Override
            public void beforeLine(Advice advice, int linenumber) {
//                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start-----beforeLine" );
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                Object[] reqObj = advice.getParameterArray();
                if (Integer.parseInt( entity.getLineNumber() ) == linenumber) {
                    log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，调用方法入参为：{}", threadId, threadName, entity.getClassName(), entity.getMethodName(), parameterStr.toString() );
                    Object object = advice.getReturnObj();
                    log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{},请求方法：{}#{}，接口返回：{}", threadId, threadName, entity.getClassName(), entity.getMethodName(), JSON.toJSONString( object ) );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，执行到类{}的{}方法中的第{}行代码", threadId, threadName, entity.getClassName(), entity.getMethodName(), linenumber );

            }


        } );
    }
}
