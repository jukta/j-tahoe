package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.LibraryResourcesFilter;
import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
    public JTahoeViewResolver viewResolver() {
        return new JTahoeViewResolver(blockFactory(), executor());
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

}
