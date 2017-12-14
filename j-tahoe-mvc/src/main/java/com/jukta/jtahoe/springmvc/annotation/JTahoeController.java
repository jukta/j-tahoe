package com.jukta.jtahoe.springmvc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by Sergey on 11/3/2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JTahoeController {
}
