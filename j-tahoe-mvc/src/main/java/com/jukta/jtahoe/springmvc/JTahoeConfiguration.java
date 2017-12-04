package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.*;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.*;

/**
 * @since 1.0
 */
@Configuration
@Order(0)
public class JTahoeConfiguration extends WebMvcConfigurerAdapter {


    @Value("${jtahoe.factory.runtime:true}")
    private boolean runtime;

    @Value("${jtahoe.executor.maxThreads:4}")
    private int maxThreads;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean
    public BlockFactory blockFactory() {
        if (!runtime) {
            return new BlockFactory();
        } else {
            return new RuntimeBlockFactory(new XthBlockModelProvider());
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public Executor executor() {
        return new ThreadPoolExecutor(0, maxThreads, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    @Bean
    @ConditionalOnMissingBean
    public DataHandlerProviderFactory dataHandlerProvider() {
        return new SpringContextDataHandlerProviderFactory(applicationContext, executor());
    }

    @Bean
    @ConditionalOnMissingBean
    public AttrsBuilder attrsBuilder() {
        return new AttrsBuilder(dataHandlerProvider(), applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public BlockRenderer blockRenderer() {
        return new BlockRenderer(blockFactory().getClassLoader());
    }


    @Bean
    @ConditionalOnMissingBean
    public JTahoeViewResolver viewResolver() {
        return new JTahoeViewResolver(blockFactory(), attrsBuilder(), blockRenderer());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public FilterRegistrationBean libraryResourcesFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LibraryResourcesFilter("app"));
        registration.addUrlPatterns("/*");
//        registration.addInitParameter("paramName", "paramValue");
        registration.setName("libraryResourcesFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public DependenciesControllerBlockHandler dependenciesControllerBlockHandler() {
        return new DependenciesControllerBlockHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public RemoteStubController remoteStubController() {
        return new RemoteStubController();
    }

}
