package com.sdk4.boot.config;

import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;

/**
 * FastJson 配置
 *
 * @author sh
 */
public class FastJsonConfigration {
    public static SerializeConfig serializeConfig = new SerializeConfig();

    static {
        // https://github.com/alibaba/fastjson/wiki/PropertyNamingStrategy_cn
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

}
