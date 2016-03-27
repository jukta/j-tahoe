package com.jukta.jtahoe.springmvc;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new JTahoeView(s);
    }
}
