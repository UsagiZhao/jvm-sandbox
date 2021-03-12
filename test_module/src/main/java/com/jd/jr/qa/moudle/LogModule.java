package com.jd.jr.qa.moudle;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.alibaba.jvm.sandbox.api.ProcessController.throwsImmediately;
import static com.alibaba.jvm.sandbox.api.event.Event.Type.BEFORE;

/**
 * Created by Gochin on 2021/2/8.
 */

@MetaInfServices(Module.class)
@Information(id = "log-module", version = "0.0.1", author = "zhaoguochun@jd.com")
public class LogModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    /**
     * 日志输出，默认采用logback，这里的日志输出到切入的服务日志中
     */
    private Logger logger = LoggerFactory.getLogger( LogModule.class.getName() );


    /**
     * 注入异常
     * @param param  sandbox 命令带参
     */
    //  -d 'LogModule/addlog?class=XXX&method=XXX'
    @Command("addlog")
    public void addlog(final Map<String, String> param) {
        // --- 解析参数 ---
        final String cnPattern = param.get( "class" );
        final String mnPattern = param.get( "method" );

        // --- 开始增强 ---
        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher )
                .onClass( cnPattern )
                .onBehavior( mnPattern )
                .onWatch( new AdviceListener() {
            @Override
            public void after(Advice advice) {
                logger.info( "Sand Box addlog start" );
                Object[] parameters = advice.getParameterArray();
                int threadId = advice.getProcessId();
                StringBuilder parameterStr = new StringBuilder();
                for (int i = 0; i < parameters.length; i++) {
                    parameterStr.append( "第" + i + "个入参为：" ).append( parameters[i] ).append( "\n" );
                }
                logger.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，请求方法：{}#{},\n入参：{}", threadId, cnPattern, mnPattern, parameterStr.toString() );
                Object object = advice.getReturnObj();
                logger.info( Constants.Sandbox_Default_LogInfo + "进程号：{}，请求方法：{}#{},接口返回：{}", threadId, cnPattern, mnPattern, object );

            }
        } );
    }
}
