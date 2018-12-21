package com.sdk4.boot.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.sdk4.common.id.IdUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Druid 配置
 *
 * @author sh
 */
@Configuration
public class DruidConfigration {
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSourceOne(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    public ServletRegistrationBean DruidStatViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        // bean.addInitParameter("allow","127.0.0.1");
        bean.addInitParameter("loginUsername", IdUtils.fastUUID().toString());
        bean.addInitParameter("loginPassword",IdUtils.fastUUID().toString());
        bean.addInitParameter("resetEnable","false");
        return bean;
    }
}
