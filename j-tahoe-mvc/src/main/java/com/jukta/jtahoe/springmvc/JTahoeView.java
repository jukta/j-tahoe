package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private Block block;

    private AttrsBuilder attrsBuilder;
    private BlockRenderer blockRenderer;

    public JTahoeView(Block block, AttrsBuilder attrsBuilder, BlockRenderer blockRenderer) {
        this.block = block;
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
        blockRenderer.render(block, attrs, httpservletrequest, httpservletresponse);
    }

}
