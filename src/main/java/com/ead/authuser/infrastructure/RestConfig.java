package com.ead.authuser.infrastructure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = "com.ead.authuser.infrastructure")
public class RestConfig implements WebMvcConfigurer {
    public RestConfig() {
        super();
    }
}
