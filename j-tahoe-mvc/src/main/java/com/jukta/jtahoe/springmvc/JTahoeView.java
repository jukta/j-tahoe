package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.*;
import com.jukta.jtahoe.jschema.JElement;
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
    private String viewName;

    private BlockFactory blockFactory;
    private DataHandlerProvider handlerProvider;
    private ApplicationContext applicationContext;

    public JTahoeView(String viewName, BlockFactory blockFactory) {
        this.viewName = viewName;
        this.blockFactory = blockFactory;
    }

    public void setHandlerProvider(DataHandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
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

        Attrs attrs = buildAttrs(map, httpservletrequest);
        JElement el = render(attrs);

        httpservletresponse.setCharacterEncoding("UTF-8");
        httpservletresponse.setContentType(getContentType());
        httpservletresponse.getWriter().write(el.toHtml());
    }

    Attrs buildAttrs(Map<String, ?> map, HttpServletRequest httpservletrequest) {
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

    JElement render(Attrs attrs) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Thread.currentThread().setContextClassLoader(blockFactory.getClassLoader());
        Block block = blockFactory.create(viewName);

        BlockHandler blockHandler = attrs.getBlockHandler();
        if (blockHandler != null) {
            blockHandler.startRendering(attrs);
        }

        JElement el = block.body(attrs);

        attrs.getDataHandlerProvider().await();

        if (blockHandler != null) {
            blockHandler.stopRendering(attrs, el);
        }

        return el;
    }

}
