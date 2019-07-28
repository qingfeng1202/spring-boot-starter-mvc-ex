package com.mvc.config;

import com.mvc.aop.WebDataAspect;
import com.mvc.aop.WebResponseBodyAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class MvcExConfiguration {

    @Bean
    public WebResponseBodyAdvice webResponseBodyAdviceInit() {
        return new WebResponseBodyAdvice();
    }

    @Bean
    public WebDataAspect webDataAspectInit(){
        return new WebDataAspect();
    }

}
