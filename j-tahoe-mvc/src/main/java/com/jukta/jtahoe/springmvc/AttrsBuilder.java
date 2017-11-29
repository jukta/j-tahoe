package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Sergey on 11/24/2017.
 */
public class AttrsBuilder {

    private DataHandlerProviderFactory dataHandlerProviderFactory;
    private ApplicationContext applicationContext;

    public AttrsBuilder(DataHandlerProviderFactory dataHandlerProviderFactory, ApplicationContext applicationContext) {
        this.dataHandlerProviderFactory = dataHandlerProviderFactory;
        this.applicationContext = applicationContext;
    }

    public Attrs build(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        Attrs attrs = new Attrs();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            attrs.set(entry.getKey(), entry.getValue());
        }

        attrs.setAttribute("session", httpservletrequest.getSession());
        attrs.setAttribute("request", httpservletrequest);

        attrs.setBlockHandler(new SpringContextBlockHandler(applicationContext));
        attrs.setDataHandlerProvider(dataHandlerProviderFactory.create(attrs));
        return attrs;

    }

}
