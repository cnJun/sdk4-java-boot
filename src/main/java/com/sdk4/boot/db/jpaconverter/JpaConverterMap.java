package com.sdk4.boot.db.jpaconverter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.Map;

/**
 * 数据库字段到 Map 类型的映射转换
 */
public class JpaConverterMap implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        String str = null;

        if (attribute != null && attribute.size() > 0) {
            str = JSON.toJSONString(attribute);
        }

        return str;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        Map<String, Object> map = null;

        if (StringUtils.isNotEmpty(dbData)) {
            map = JSON.parseObject(dbData, Map.class);
        } else {
            map = Maps.newHashMap();
        }

        return map;
    }
}
