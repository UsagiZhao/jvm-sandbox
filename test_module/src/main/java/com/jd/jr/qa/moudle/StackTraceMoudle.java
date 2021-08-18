package com.jd.jr.qa.moudle;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Gochin on 2021/3/19.
 */
@MetaInfServices(Module.class)
@Information(id = "stack-module", version = "0.0.1", author = "zhaoguochun@jd.com")
@Slf4j
public class StackTraceMoudle implements Module {

    private final String jdPackageName = "com.jd.jr";
    private final String qaPackageName = "com.jd.jr.qa";
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("businessflow")
    public void businessFlow(final Map<String, String> param, final PrintWriter writer) {

        // --- 解析参数 ---
        final String cnPattern = param.get( "class" );
        final String mnPattern = param.get( "method" );


        new EventWatchBuilder( moduleEventWatcher )
                .onClass( cnPattern )
                .onBehavior( mnPattern )
                .onWatch( new AdviceListener() {
            @Override
            public void after(Advice advice) {
                StackTraceElement[] stackTraceInfo = Thread.currentThread().getStackTrace();
                List<String> traceList = Lists.newArrayList();
                String className = "";
                String stackInfo = "";
                for (StackTraceElement element : stackTraceInfo) {
                    className = element.getClassName();
                    if(element.getMethodName().equals( "invoke" )){
                        continue;
                    }
                    if (className.contains( jdPackageName ) && !className.contains( qaPackageName )) {
                        stackInfo =  className + "#" + element.getMethodName() + "[" + element.getLineNumber() + "]";
                        traceList.add(stackInfo);
                    }
                }
                //将调用过程顺序对调
                Collections.reverse(traceList);
                traceList.forEach( s -> log.info( "Business StackTrace {}", s ) );
            }
        } );
    }

}
