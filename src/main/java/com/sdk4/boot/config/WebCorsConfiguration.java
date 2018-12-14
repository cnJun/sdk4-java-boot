package com.sdk4.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * 跨域配置
 *
 * https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Access_control_CORS
 *
 * @author sh
 */
@Slf4j
@Configuration
public class WebCorsConfiguration {

    @Value("${sdk4.cors.enable:false}")
    private boolean enable;

    @Value("${sdk4.cors.allowedOrigin:*}")
    private String allowedOrigin;

    @Value("${sdk4.cors.allowedHeader:*}")
    private String allowedHeader;

    @Value("${sdk4.cors.allowedMethod:*}")
    private String allowedMethod;

    @Value("${sdk4.cors.url:/**}")
    private List<String> urls;

    private CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(allowedOrigin);
        corsConfiguration.addAllowedHeader(allowedHeader);
        corsConfiguration.addAllowedMethod(allowedMethod);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        if (enable && urls != null) {
            CorsConfiguration corsConfiguration = corsConfiguration();
            for (String url : urls) {
                source.registerCorsConfiguration(url, corsConfiguration);

                log.info("launch cors: {}", url);
            }
        }

        return new CorsFilter(source);
    }
}
