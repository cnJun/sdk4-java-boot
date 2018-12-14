package com.sdk4.boot.config;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.springframework.context.annotation.Configuration;

/**
 * FastJson 配置
 *
 * @author sh
 */
@Configuration
public class FastJsonConfigration {
    public static final SerializeConfig serializeConfig = new SerializeConfig();

    static {
        // https://github.com/alibaba/fastjson/wiki/PropertyNamingStrategy_cn
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

}
