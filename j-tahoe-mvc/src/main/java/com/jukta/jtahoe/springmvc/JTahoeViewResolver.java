package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.BlockFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver, InitializingBean {

    private BlockFactory blockFactory;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new JTahoeView(s, blockFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        blockFactory = new BlockFactory();
    }

    public BlockFactory getBlockFactory() {
        return blockFactory;
    }
}
