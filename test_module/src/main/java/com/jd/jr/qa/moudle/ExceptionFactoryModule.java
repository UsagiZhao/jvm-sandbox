package com.jd.jr.qa.moudle;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.http.printer.ConcurrentLinkedQueuePrinter;
import com.alibaba.jvm.sandbox.api.http.printer.Printer;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.utils.CommonUtils;
import com.jd.jr.qa.utils.ProgressPrinter;
import java.util.logging.Logger;
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
@Information(id = "exception-module", version = "0.0.1", author = "zhaoguochun@jd.com")
public class ExceptionFactoryModule implements Module {

    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    /**
     * 日志输出，默认采用logback，这里的日志输出到切入的服务日志中
     */
    private Logger logger = Logger.getLogger( ExceptionFactoryModule.class.getName() );


    /**
     * 异常工厂
     */
    interface ExceptionFactory {
        Exception newInstance(String message);
    }

    /**
     * 异常类型
     */
    enum ExceptionType {
        IOException( new ExceptionFactoryModule.ExceptionFactory() {
            @Override
            public Exception newInstance(String message) {
                return new IOException( message );
            }
        } ), NullPointException( new ExceptionFactoryModule.ExceptionFactory() {
            @Override
            public Exception newInstance(String message) {
                return new NullPointerException( message );
            }
        } ), RuntimeException( new ExceptionFactoryModule.ExceptionFactory() {
            @Override
            public Exception newInstance(String message) {
                return new RuntimeException( message );
            }
        } ), TimeoutException( new ExceptionFactoryModule.ExceptionFactory() {
            @Override
            public Exception newInstance(String message) {
                return new TimeoutException( message );
            }
        } );

        private final ExceptionFactoryModule.ExceptionFactory factory;

        ExceptionType(final ExceptionFactoryModule.ExceptionFactory factory) {
            this.factory = factory;
        }

        public Exception throwIt(final String message) throws Exception {
            return factory.newInstance( message );
        }

    }


    /*
     * 注入异常
     * -d 'ExceptionFactoryMoudle/injectException?class=<CLASS>&method=<METHOD>&type=<EXCEPTION-TYPE>'
     * "ExceptionFactoryMoudle/injectException?class=com.jd.jr.cis.user.rpc.impl.IdentityAuthenticationServiceRpcImpl&method=verifyCredentialsNoAndNameV3&type=NullPointException"
     */
    @Command("injectException")
    public void injectException(final Map<String, String> param, final PrintWriter writer) {

        final Printer printer = new ConcurrentLinkedQueuePrinter( writer );

        // --- 解析参数 ---

        final String cnPattern = param.get( "class" );
        final String mnPattern = param.get( "method" );
        final String exceptiontype = param.get( "type" );
        final String message = param.get( "message" );
        final ExceptionFactoryModule.ExceptionType exType = EnumUtils.getEnum( ExceptionFactoryModule.ExceptionType.class, exceptiontype );


        // --- 开始增强 ---

        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher )
                .onClass( cnPattern )
                .includeSubClasses()
                .includeBootstrap()
                .onBehavior( mnPattern )
                .onWatching()
                .withProgress( new ProgressPrinter( printer ) )
                .onWatch( new EventListener() {
            @Override
            public void onEvent(Event event) throws Throwable {

                final BeforeEvent bEvent = (BeforeEvent) event;
                logger.info( bEvent.javaClassName + "#" + bEvent.javaMethodName + "将被注入 " + exType.name() + "（线程名称：" + Thread.currentThread().getName() + "）");
                //抛出异常
                throwsImmediately( exType.throwIt( StringUtils.isEmpty( message ) ? message : "mock 异常注入" ) );
            }
        }, BEFORE );

        // --- 等待结束 ---

        try {
            logger.info( "exception on ["+cnPattern+"#"+mnPattern+"] exception: "+exType.name()+".\nPress CTRL_C abort it!");
//            printer.println(String.format(
//                    "exception on [%s#%s] exception: %s.\nPress CTRL_C abort it!",
//                    cnPattern,
//                    mnPattern,
//                    exType.name()
//            ));
            printer.waitingForBroken();
        } finally {
            watcher.onUnWatched();
        }

    }
}
