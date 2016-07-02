package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
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

    public JTahoeView(String viewName) {
        this.viewName = viewName;
    }

    public JTahoeView(String viewName, ClassLoader classLoader) {
        this.viewName = viewName;
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
        Block block = newBlockInstance();
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

    private Block newBlockInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (classLoader == null) {
            return (Block) Class.forName(viewName).newInstance();
        } else {
            return (Block) Class.forName(viewName, true, classLoader).newInstance();
        }
    }
}
