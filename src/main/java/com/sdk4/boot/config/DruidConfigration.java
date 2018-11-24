package com.sdk4.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Druid 配置
 *
 * @author sh
 */
public class DruidConfigration {
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSourceOne(){
        DruidDataSource ds = DruidDataSourceBuilder.create().build();
        return ds;
    }
}
