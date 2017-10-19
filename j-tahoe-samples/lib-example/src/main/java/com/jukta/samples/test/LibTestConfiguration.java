package com.jukta.samples.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sergey on 10/19/2017.
 */
@Configuration
public class LibTestConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ButtonDataHandler buttonDataHandler() {
        return new ButtonDataHandler();
    }

}
