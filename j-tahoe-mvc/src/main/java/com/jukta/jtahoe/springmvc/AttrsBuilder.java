package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandlerProvider;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Sergey on 11/24/2017.
 */
public class AttrsBuilder {

    private DataHandlerProvider handlerProvider;
    private ApplicationContext applicationContext;

    public AttrsBuilder(DataHandlerProvider handlerProvider, ApplicationContext applicationContext) {
        this.handlerProvider = handlerProvider;
        this.applicationContext = applicationContext;
    }

    public Attrs build(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) {
        Attrs attrs = new Attrs();
        attrs.setDataHandlerProvider(handlerProvider);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            attrs.set(entry.getKey(), entry.getValue());
        }

        attrs.setAttribute("session", httpservletrequest.getSession());
        attrs.setAttribute("request", httpservletrequest);

        attrs.setBlockHandler(new SpringContextBlockHandler(applicationContext));
        return attrs;

    }

}
