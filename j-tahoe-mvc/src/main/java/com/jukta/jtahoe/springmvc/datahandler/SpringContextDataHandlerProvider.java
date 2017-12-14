package com.jukta.jtahoe.springmvc.datahandler;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.DataHandler;
import com.jukta.jtahoe.DefaultDataHandlerProvider;
import com.jukta.jtahoe.springmvc.annotation.JTahoeController;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletResponseMethodArgumentResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class SpringContextDataHandlerProvider extends DefaultDataHandlerProvider {
    private ApplicationContext applicationContext;

    private Map<String, ServletInvocableHandlerMethod> dh;

    public SpringContextDataHandlerProvider(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        init();
    }

    private void init() {
        dh = new HashMap<>();

        HandlerMethodArgumentResolverComposite methodResolverComposite = new HandlerMethodArgumentResolverComposite();
        methodResolverComposite.addResolver(new ServletRequestMethodArgumentResolver());
        methodResolverComposite.addResolver(new ServletResponseMethodArgumentResolver());
        methodResolverComposite.addResolver(new AbstractNamedValueMethodArgumentResolver() {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return new NamedValueInfo("arg"+parameter.getParameterIndex(), true, null);
            }

            @Override
            protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
                Attrs attrs = (Attrs) request.getAttribute("__attrs", RequestAttributes.SCOPE_REQUEST);
                return attrs;
            }

            @Override
            protected void handleMissingValue(String s, MethodParameter methodParameter) throws ServletException {

            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return parameter.getParameterType().equals(Attrs.class);
            }

        });

        WebDataBinderFactory factory = new DefaultDataBinderFactory(new ConfigurableWebBindingInitializer());


        for (Map.Entry<String, Object> entry : applicationContext.getBeansWithAnnotation(JTahoeController.class).entrySet()) {
            Object bean = entry.getValue();
            for (Method m : bean.getClass().getDeclaredMethods()) {
                com.jukta.jtahoe.springmvc.annotation.DataHandler  a = m.getDeclaredAnnotation(com.jukta.jtahoe.springmvc.annotation.DataHandler.class);
                if (a != null) {
                    String dhName = a.value();
                    if ("".equals(dhName)) {
                        dhName = m.getName();
                    }
                    ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(bean, m);
                    handlerMethod.setHandlerMethodArgumentResolvers(methodResolverComposite);
                    handlerMethod.setDataBinderFactory(factory);
                    dh.put(dhName, handlerMethod);
                }

            }
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public DataHandler getDataHandler(String dataHandler, Attrs attrs) {

        InvocableHandlerMethod handlerMethod = dh.get(dataHandler);
        if (handlerMethod != null) {
            com.jukta.jtahoe.springmvc.annotation.DataHandler dha = handlerMethod.getMethodAnnotation(com.jukta.jtahoe.springmvc.annotation.DataHandler.class);
            return new DataHandler() {
                @Override
                public Attrs getData(Attrs attrs) {
                    HttpServletRequest request = (HttpServletRequest) attrs.getAttribute("request");
                    request.setAttribute("__attrs", attrs);
                    HttpServletResponse response = (HttpServletResponse) attrs.getAttribute("response");
                    ServletWebRequest webRequest = new ServletWebRequest(request, response);

                    try {
                        Object val = handlerMethod.invokeForRequest(webRequest, null);
                        if (val != null && val instanceof Attrs) {
                            return attrs;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return attrs;
                }

                @Override
                public boolean async() {
                    return dha.async();
                }
            };
        } else {
            return applicationContext.getBean(dataHandler, DataHandler.class);
        }
    }
}
