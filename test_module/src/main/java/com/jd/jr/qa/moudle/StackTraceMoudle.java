package com.jd.jr.qa.moudle;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Gochin on 2021/3/19.
 */
@MetaInfServices(Module.class)
@Information(id = "stack-module", version = "0.0.1", author = "zhaoguochun@jd.com")
public class StackTraceMoudle {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger( StackTraceMoudle.class );
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command("injectException")
    public void injectException(final Map<String, String> param, final PrintWriter writer) {

        // --- 解析参数 ---
        final String cnPattern = param.get( "class" );
        final String mnPattern = param.get( "method" );


    }

}
