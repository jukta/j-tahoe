package com.jukta.jtahoe.springmvc.aloha;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by Sergey on 11/3/2017.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RemoteEvent {
    String value();
    String responseEvent() default "";
}
