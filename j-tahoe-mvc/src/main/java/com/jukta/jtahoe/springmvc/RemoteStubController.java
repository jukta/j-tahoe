package com.jukta.jtahoe.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey on 11/3/2017.
 */
@Controller
public class RemoteStubController {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, InvocableHandlerMethod> map;

    private ObjectMapper mapper = new ObjectMapper();

    @PostConstruct
    private void init() {
        map = new HashMap<>();

        HandlerMethodArgumentResolverComposite argumentResolverComposite = new HandlerMethodArgumentResolverComposite();
        argumentResolverComposite.addResolver(new AbstractNamedValueMethodArgumentResolver() {
            @Override
            protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
                return new NamedValueInfo("arg"+parameter.getParameterIndex(), true, null);
            }

            @Override
            protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
                Class type = parameter.getParameterType();
                String val = request.getParameter(name);
                if (isPrimitive(type)) {
                    return val;
                } else {

                    return mapper.readValue(val, type);
                }
            }

            @Override
            protected void handleMissingValue(String s, MethodParameter methodParameter) throws ServletException {

            }

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return true;
            }

        });

        WebDataBinderFactory factory = new DefaultDataBinderFactory(new ConfigurableWebBindingInitializer());


        for (Map.Entry<String, Object> entry : applicationContext.getBeansWithAnnotation(TahoeController.class).entrySet()) {
            Object bean = entry.getValue();
            for (Method m : bean.getClass().getDeclaredMethods()) {
                ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(bean, m);
                handlerMethod.setHandlerMethodArgumentResolvers(argumentResolverComposite);
                handlerMethod.setDataBinderFactory(factory);
                map.put(m.getName(), handlerMethod);
            }
        }
    }

    @RequestMapping(value = "/__bean/{name}/{method}", method = RequestMethod.POST)
    public void index(@PathVariable("name") String name,
                        @PathVariable("method") String method,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        InvocableHandlerMethod handlerMethod = map.get(method);
        Object val = handlerMethod.invokeForRequest(webRequest, null);
        if (val != null && !isPrimitive(val.getClass())) {
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(), val);
        } else if (val != null){
            response.getOutputStream().write(val.toString().getBytes());
        }
        response.getOutputStream().close();
    }

    private boolean isPrimitive(Class c) {
        return c.getPackage().getName().equals("java.lang");
    }

}
