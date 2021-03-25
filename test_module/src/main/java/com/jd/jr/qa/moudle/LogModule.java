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
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Created by Gochin on 2021/2/8.
 */

@MetaInfServices(Module.class)
@Information(id = "log-module", version = "0.0.2", author = "zhaoguochun@jd.com")
public class LogModule implements Module {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger( LogModule.class );
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * 注入异常
     *
     * @param param sandbox 命令带参
     */
    //  -d 'log-module/addlog?class=XXX&method=XXX'
    @Command("addlog")
    public void addlog(final Map<String, String> param) {
        // --- 解析参数 ---
        final String cnPattern = param.get( "class" );
        final String mnPattern = param.get( "method" );
        final String lineNumber = param.get( "lineNumber" );

        // --- 开始增强 ---
        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher ).onClass( cnPattern ).onBehavior( mnPattern ).onWatch( new AdviceListener() {
            @Override
            public void before(Advice advice) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start -----before" );
                Object[] parameters = advice.getParameterArray();
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                for (int i = 0; i < parameters.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参为：" ).append( parameters[i] ).append( "\n" );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，原始入参为：{}", threadId, threadName, cnPattern, mnPattern, parameterStr.toString() );
            }

            @Override
            public void after(Advice advice) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start-----after" );
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                Object[] reqObj = advice.getParameterArray();
                for (int i = 0; i < reqObj.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参为：" ).append( reqObj[i] ).append( "\n" );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，调用方法入参为：{}", threadId, threadName, cnPattern, mnPattern, parameterStr.toString() );
                Object object = advice.getReturnObj();
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{},请求方法：{}#{}，接口返回：{}", threadId, threadName, cnPattern, mnPattern, JSON.toJSONString( object ) );
            }

            @Override
            public void beforeLine(Advice advice,int linenumber) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start-----beforeLine" );
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                Object[] reqObj = advice.getParameterArray();
                for (int i = 0; i < reqObj.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参为：" ).append( reqObj[i] ).append( "\n" );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，调用方法入参为：{}", threadId, threadName, cnPattern, mnPattern, parameterStr.toString() );
                Object object = advice.getReturnObj();
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{},请求方法：{}#{}，接口返回：{}", threadId, threadName, cnPattern, mnPattern, JSON.toJSONString( object ) );
            }


        } );
    }
}
