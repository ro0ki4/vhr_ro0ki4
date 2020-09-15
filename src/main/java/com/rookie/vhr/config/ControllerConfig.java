package com.rookie.vhr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ro0ki4
 * @data 2020/9/2 13:11
 * version 1.0
 */
@Configuration
public class ControllerConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
//                registry.addViewController("/login").setViewName("/index.html");
                registry.addViewController("/").setViewName("/index.html");
                registry.addViewController("/index").setViewName("/index.html");
            }
        };
    }
}
