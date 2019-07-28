package com.mvc.annotation;

import com.mvc.config.MvcExConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({MvcExConfiguration.class})
public @interface MvcEx {
}
