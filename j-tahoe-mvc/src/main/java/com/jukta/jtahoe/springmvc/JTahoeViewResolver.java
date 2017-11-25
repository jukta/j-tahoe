package com.jukta.jtahoe.springmvc;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver {

    private AttrsBuilder attrsBuilder;
    private BlockRenderer blockRenderer;

    public JTahoeViewResolver() {
    }

    public JTahoeViewResolver(AttrsBuilder attrsBuilder, BlockRenderer blockRenderer) {
        this.attrsBuilder = attrsBuilder;
        this.blockRenderer = blockRenderer;
    }

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        JTahoeView view = new JTahoeView(s, attrsBuilder, blockRenderer);
        return view;
    }

}
