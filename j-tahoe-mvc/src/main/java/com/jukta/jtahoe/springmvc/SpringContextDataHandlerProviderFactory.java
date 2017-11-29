package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandlerProvider;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executor;

/**
 * @author Sergey Sidorov
 */
public class SpringContextDataHandlerProviderFactory implements DataHandlerProviderFactory {

    private ApplicationContext applicationContext;
    private Executor executor;

    public SpringContextDataHandlerProviderFactory(ApplicationContext applicationContext, Executor executor) {
        this.applicationContext = applicationContext;
        this.executor = executor;
    }

    public DataHandlerProvider create(Attrs attrs) {
        SpringContextDataHandlerProvider provider = new SpringContextDataHandlerProvider(applicationContext);
        provider.setExecutor(executor);
        return provider;
    }

}
