package com.jukta.samples.test;

import com.jukta.jtahoe.Attrs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sergey on 10/19/2017.
 */
@Configuration
public class MvcTestConfiguration {

    @Bean
    public ButtonDataHandler buttonDataHandler() {
        return new ButtonDataHandler() {
            @Override
            public Attrs getData(Attrs attrs) {
                attrs.set("message", "This is button from app");
                return attrs;
            }
        };
    }

}
