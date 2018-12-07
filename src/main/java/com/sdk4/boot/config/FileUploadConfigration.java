package com.sdk4.boot.config;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.fileupload.FileUploadConfig;
import com.sdk4.boot.fileupload.FileUploadHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 加载文件上传配置
 *
 * @author sh
 */
@Slf4j
@Configuration
public class FileUploadConfigration {

    @Bean
    @ConfigurationProperties(prefix = "sdk4.fileupload")
    public FileUploadConfig fileUploadConfig() {
        return new FileUploadConfig();
    }

    @Autowired
    public void loadFileUploadConfig(FileUploadConfig fileUploadConfig) {
        FileUploadHelper.loadConfig(fileUploadConfig);
        log.info("load fileupload config: {}", JSON.toJSONString(fileUploadConfig));
    }

}
