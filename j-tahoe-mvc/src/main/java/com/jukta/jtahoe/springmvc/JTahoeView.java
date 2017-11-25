package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private String viewName;

    private AttrsBuilder attrsBuilder;
    private BlockRenderer blockRenderer;

    public JTahoeView(String viewName, AttrsBuilder attrsBuilder, BlockRenderer blockRenderer) {
        this.viewName = viewName;
        this.attrsBuilder = attrsBuilder;
        this.blockRenderer = blockRenderer;
    }

    @Override
    public String getContentType() {
        return blockRenderer.getContentType();
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws Exception {

        Attrs attrs = attrsBuilder.build(map, httpservletrequest, httpservletresponse);
        blockRenderer.render(viewName, attrs, httpservletrequest, httpservletresponse);
    }

}
