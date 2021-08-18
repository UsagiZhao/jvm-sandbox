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
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.jd.jr.qa.constants.Constants;

import com.jd.jr.qa.entity.ExceptionFactoryRequestEntity;
import com.jd.jr.qa.entity.LogRequestEntity;
import com.jd.jr.qa.utils.MapUtils;
import com.jd.jr.qa.utils.validator.ExceptionFactoryMoudleValidator;
import com.jd.jr.qa.utils.validator.MethodMockMoudleValidator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExceptionFactoryModule implements Module {
    @Resource
    private ConfigInfo configInfo;
    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    private final ExceptionFactoryMoudleValidator exceptionFactoryMoudleValidator = new ExceptionFactoryMoudleValidator();

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
     */
    @Command("injectException")
    public void injectException(final Map<String, String> param, final PrintWriter writer) throws Exception {



        final Printer printer = new ConcurrentLinkedQueuePrinter( writer );
//        final String cnPattern = param.get( "className" );
//        final String mnPattern = param.get( "methodName" );
//        final String exceptiontype = param.get( "type" );
//        final String message = param.get( "message" );
        //解析参数并拼装
        final ExceptionFactoryRequestEntity entity = MapUtils.mapToEntity( ExceptionFactoryRequestEntity.class, param, "className", "methodName","exceptionType","message" );
        //异常类型
        final ExceptionFactoryModule.ExceptionType exType = EnumUtils.getEnum( ExceptionFactoryModule.ExceptionType.class, entity.getExceptionType() );
        //校验参数
        exceptionFactoryMoudleValidator.validate( entity );

        // --- 开始增强 ---
        final EventWatcher watcher = new EventWatchBuilder( moduleEventWatcher )
                .onClass( entity.getClassName() )
                .includeSubClasses()
                .includeBootstrap()
                .onBehavior( entity.getMethodName() )
                .onWatching()
                .onWatch( new EventListener() {
            @Override
            public void onEvent(Event event) throws Throwable {

                final BeforeEvent bEvent = (BeforeEvent) event;
                log.info( Constants.Sandbox_Default_LogInfo+"{}#{}在调用前将抛出{}异常。（线程名称：{}）", bEvent.javaClassName, bEvent.javaMethodName, exType.name(), Thread.currentThread().getName() );
                //抛出异常
                throwsImmediately( exType.throwIt( StringUtils.isEmpty( entity.getMessage() ) ? entity.getMessage() : "mock 异常注入" ) );
            }
        }, BEFORE );

        // --- 等待结束 ---

        try {
//            log.info( "exception on [" + cnPattern + "#" + mnPattern + "] exception: " + exType.name() + ".\nPress CTRL_C abort it!" );
            log.info( "exception on [{}#{}] exception:{} \nPress CTRL_C abort it!", entity.getClassName(), entity.getMethodName(), exType.name() );
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
