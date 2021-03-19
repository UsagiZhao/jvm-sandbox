package com.jd.jr.qa.moudle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.http.printer.ConcurrentLinkedQueuePrinter;
import com.alibaba.jvm.sandbox.api.http.printer.Printer;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.constants.Constants;
import com.jd.jr.qa.utils.CommonUtils;
import com.jd.jr.qa.utils.ProgressPrinter;

import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import static com.alibaba.jvm.sandbox.api.ProcessController.throwsImmediately;
import static com.alibaba.jvm.sandbox.api.event.Event.Type.BEFORE;

/**
 * Created by Gochin on 2021/2/8.
 */

@Slf4j
@MetaInfServices(Module.class)
@Information(id = "log-module", version = "0.0.2", author = "zhaoguochun@jd.com")
public class LogModule implements Module {

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

        // --- 开始增强 ---
        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher ).onClass( cnPattern ).onBehavior( mnPattern ).onWatch( new AdviceListener() {
            @Override
            public void after(Advice advice) {
                log.info( Constants.Sandbox_Default_LogInfo + "Sand Box addlog start" );
                Object[] parameters = advice.getParameterArray();
                int threadId = advice.getProcessId();
                String threadName = Thread.currentThread().getName();
                StringBuilder parameterStr = new StringBuilder();
                for (int i = 0; i < parameters.length; i++) {
                    parameterStr.append( "第" + Math.addExact( i, 1 ) + "个入参为：" ).append( parameters[i] ).append( "\n" );
                }
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{}，请求方法：{}#{}，入参：{}", threadId, threadName, cnPattern, mnPattern, parameterStr.toString() );
                Object object = advice.getReturnObj();
                log.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，进程名：{},请求方法：{}#{}，接口返回：{}", threadId, threadName, cnPattern, mnPattern, JSON.toJSONString( object ) );

            }
        } );
    }
}
