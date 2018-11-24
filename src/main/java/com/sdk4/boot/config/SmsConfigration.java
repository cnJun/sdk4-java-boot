package com.sdk4.boot.config;

import com.alibaba.fastjson.JSON;
import com.sdk4.sms.SmsConfig;
import com.sdk4.sms.SmsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加载短信配置
 *
 * @author sh
 */
@Slf4j
@Configuration
public class SmsConfigration {

    @Bean
    @ConfigurationProperties(prefix = "sdk4.sms")
    public SmsConfig smsConfig() {
        return new SmsConfig();
    }

    @Autowired
    public void loadSmsConfig(SmsConfig smsConfig) {
        SmsHelper.loadConfig(smsConfig);
        log.info("load sms config: {}", JSON.toJSONString(smsConfig));
    }

}
