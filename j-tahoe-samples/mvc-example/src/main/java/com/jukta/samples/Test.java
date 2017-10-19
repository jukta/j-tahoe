package com.jukta.samples;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Sergey Sidorov
 */
@SpringBootApplication
@EnableWebMvc
public class Test {

    public static void main(String[] args) throws URISyntaxException, IOException {

        SpringApplication app = new SpringApplication(Test.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
//
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//
//        Resource[] r = resolver.getResources("classpath*:/**/MANIFEST.MF");
//        for (Resource r1 : r) {
//            System.out.println(r1.getURL());
//        }

    }

}
