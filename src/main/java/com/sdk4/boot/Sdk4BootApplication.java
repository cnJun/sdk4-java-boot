package com.sdk4.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 启动入口
 *
 * @author sh
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
@EnableAspectJAutoProxy
public class Sdk4BootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Sdk4BootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Sdk4BootApplication.class, args);
    }
}
