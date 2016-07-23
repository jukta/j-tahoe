package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private String contentType = "text/html;charset=UTF-8";
    private ApplicationContext applicationContext;

    private String viewName;
    private ClassLoader classLoader;
    private  BlockFactory blockFactory;

    public JTahoeView(String viewName) {
        this.viewName = viewName;
    }

    public JTahoeView(String viewName, BlockFactory blockFactory, ClassLoader classLoader) {
        this.viewName = viewName;
        this.blockFactory = blockFactory;
        this.classLoader = classLoader;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws Exception {
        Thread.currentThread().setContextClassLoader(classLoader);
        Block block = blockFactory.create(viewName);
        MvcDataHandlerProvider handlerProvider = new MvcDataHandlerProvider(applicationContext, httpservletrequest, httpservletresponse);
        Attrs attrs = new Attrs();
        attrs.setDataHandlerProvider(handlerProvider);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            attrs.set(entry.getKey(), entry.getValue());
        }

        attrs.setAttribute("session", httpservletrequest.getSession());
        attrs.setAttribute("request", httpservletrequest);

        long st = System.nanoTime();
        String s = block.body(attrs).toHtml();
        long t = System.nanoTime() - st;
        System.out.println("Render time: " + t);
        httpservletresponse.setCharacterEncoding("UTF-8");
        httpservletresponse.setContentType(getContentType());
        httpservletresponse.getWriter().write(s);
    }

}
