package com.sdk4.boot.config;

import com.sdk4.boot.filter.RequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author sh
 */
@Configuration
public class RequestConfigure {

    @Bean
    public WebMvcConfigurer webMvcConfigurerForProfile() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new RequestFilter()).addPathPatterns("/**");
            }
        };
    }
}
