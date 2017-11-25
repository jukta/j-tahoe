package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.BlockHandler;
import com.jukta.jtahoe.jschema.JElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sergey on 11/24/2017.
 */
public class BlockRenderer {

    private String contentType = "text/html;charset=UTF-8";

    private BlockFactory blockFactory;

    public BlockRenderer(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    public JElement render(String viewName, Attrs attrs) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
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

    public void render(String viewName, Attrs attrs, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws IllegalAccessException, ClassNotFoundException, InstantiationException, IOException {
        JElement el = render(viewName, attrs);

        httpservletresponse.setCharacterEncoding("UTF-8");
        httpservletresponse.setContentType(getContentType());
        httpservletresponse.getWriter().write(el.toHtml());
        httpservletresponse.getWriter().close();
    }

    public String getContentType() {
        return contentType;
    }

}
