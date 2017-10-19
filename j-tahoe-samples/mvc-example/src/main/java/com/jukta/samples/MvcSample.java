package com.jukta.samples;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Sergey Sidorov
 */
@SpringBootApplication
@EnableWebMvc
public class MvcSample {

    public static void main(String[] args) throws URISyntaxException, IOException {

        SpringApplication app = new SpringApplication(MvcSample.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

    }

}
