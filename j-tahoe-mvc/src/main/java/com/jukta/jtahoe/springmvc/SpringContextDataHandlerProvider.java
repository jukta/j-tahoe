package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.*;
import org.springframework.context.ApplicationContext;

/**
 * @author Sergey Sidorov
 */
public class SpringContextDataHandlerProvider extends DefaultDataHandlerProvider {
    private ApplicationContext applicationContext;

    public SpringContextDataHandlerProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public DataHandler getDataHandler(String dataHandler, Attrs attrs) {
        return applicationContext.getBean(dataHandler, DataHandler.class);
    }
}
