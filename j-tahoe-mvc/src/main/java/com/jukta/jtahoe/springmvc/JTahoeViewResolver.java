package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver {

    private AttrsBuilder attrsBuilder;
    private BlockRenderer blockRenderer;
    private BlockFactory blockFactory;

    public JTahoeViewResolver(BlockFactory blockFactory, AttrsBuilder attrsBuilder, BlockRenderer blockRenderer) {
        this.blockFactory = blockFactory;
        this.attrsBuilder = attrsBuilder;
        this.blockRenderer = blockRenderer;
    }

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        Block block = blockFactory.create(s);
        JTahoeView view = new JTahoeView(block, attrsBuilder, blockRenderer);
        return view;
    }

}
