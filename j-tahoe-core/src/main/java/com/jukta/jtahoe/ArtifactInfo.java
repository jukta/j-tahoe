package com.jukta.jtahoe;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ArtifactInfo {
    String groupId() default "";
    String artifactId() default "";
    String version() default "";
}
