package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandler;
import com.jukta.jtahoe.DataHandlerProvider;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Sidorov
 */
public class MvcDataHandlerProvider implements DataHandlerProvider {
    private ApplicationContext applicationContext;
    private HttpServletRequest httpservletrequest;
    private HttpServletResponse httpservletresponse;

    public MvcDataHandlerProvider(ApplicationContext applicationContext, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        this.applicationContext = applicationContext;
        this.httpservletrequest = httpservletrequest;
        this.httpservletresponse = httpservletresponse;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Attrs getData(String dataHandler, Attrs attrs) {
        DataHandler handler = applicationContext.getBean(dataHandler, DataHandler.class);
        return handler.getData(attrs, httpservletrequest, httpservletresponse);
    }
}
