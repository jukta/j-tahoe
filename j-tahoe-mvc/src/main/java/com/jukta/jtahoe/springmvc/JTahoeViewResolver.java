package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.BlockFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private BlockFactory blockFactory;
    private Executor executor;

    public JTahoeViewResolver() {
    }

    public JTahoeViewResolver(BlockFactory blockFactory, Executor executor) {
        this.blockFactory = blockFactory;
        this.executor = executor;
    }

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        JTahoeView view = new JTahoeView(s, blockFactory);
        view.setApplicationContext(applicationContext);
        return view;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public BlockFactory getBlockFactory() {
        return blockFactory;
    }

    public void setBlockFactory(BlockFactory blockFactory) {
        this.blockFactory = blockFactory;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
}
