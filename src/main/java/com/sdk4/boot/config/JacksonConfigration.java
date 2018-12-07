package com.sdk4.boot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * Jackson配置
 *
 * @author sh
 */
@Configuration
public class JacksonConfigration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJackson() {
        return jackson2ObjectMapperBuilder -> {
            jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
            jackson2ObjectMapperBuilder.failOnUnknownProperties(false);
            jackson2ObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            jackson2ObjectMapperBuilder.timeZone(TimeZone.getTimeZone("GMT+8"));
            jackson2ObjectMapperBuilder.simpleDateFormat("yyyy-MM-dd HH:mm:ss");
        };
    }

}
