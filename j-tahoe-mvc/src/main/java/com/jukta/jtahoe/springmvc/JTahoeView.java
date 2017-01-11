package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.jschema.JElement;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private String contentType = "text/html;charset=UTF-8";
    private ApplicationContext applicationContext;
    private Executor executor;

    private String viewName;
    private  BlockFactory blockFactory;

    public JTahoeView(String viewName, BlockFactory blockFactory) {
        this(viewName, blockFactory, null);
    }

    public JTahoeView(String viewName, BlockFactory blockFactory, Executor executor) {
        this.viewName = viewName;
        this.blockFactory = blockFactory;
        this.executor = executor;
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
        Thread.currentThread().setContextClassLoader(blockFactory.getClassLoader());
        Block block = blockFactory.create(viewName);
        MvcDataHandlerProvider handlerProvider = new MvcDataHandlerProvider(applicationContext);
        handlerProvider.setExecutor(this.executor);
        Attrs attrs = new Attrs();
        attrs.setDataHandlerProvider(handlerProvider);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            attrs.set(entry.getKey(), entry.getValue());
        }

        attrs.setAttribute("session", httpservletrequest.getSession());
        attrs.setAttribute("request", httpservletrequest);

        JElement el = block.body(attrs);

        if (executor != null) handlerProvider.await();

        httpservletresponse.setCharacterEncoding("UTF-8");
        httpservletresponse.setContentType(getContentType());
        httpservletresponse.getWriter().write(el.toHtml());
    }

}
