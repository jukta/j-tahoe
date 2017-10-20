package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.DataHandlerProvider;
import org.springframework.beans.BeansException;
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
    private DataHandlerProvider handlerProvider;
    private LibraryMetaController libraryMetaController;

    public JTahoeViewResolver() {
    }

    public JTahoeViewResolver(BlockFactory blockFactory, Executor executor, DataHandlerProvider handlerProvider, LibraryMetaController libraryMetaController) {
        this.blockFactory = blockFactory;
        this.executor = executor;
        this.handlerProvider = handlerProvider;
        this.libraryMetaController = libraryMetaController;
    }

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        JTahoeView view = new JTahoeView(s, blockFactory, libraryMetaController);
        view.setHandlerProvider(handlerProvider);
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

    public DataHandlerProvider getHandlerProvider() {
        return handlerProvider;
    }

    public void setHandlerProvider(DataHandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
    }

    public LibraryMetaController getLibraryMetaController() {
        return libraryMetaController;
    }

    public void setLibraryMetaController(LibraryMetaController libraryMetaController) {
        this.libraryMetaController = libraryMetaController;
    }
}
