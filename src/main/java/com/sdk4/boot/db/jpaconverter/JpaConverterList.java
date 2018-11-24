package com.sdk4.boot.db.jpaconverter;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import java.util.List;

/**
 * 数据库字段到 List 类型的映射转换
 */
public class JpaConverterList implements AttributeConverter<List<Object>, String> {

    @Override
    public String convertToDatabaseColumn(List<Object> attribute) {
        String str = null;

        if (attribute != null && attribute.size() > 0) {
            str = JSON.toJSONString(attribute);
        }

        return str;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> convertToEntityAttribute(String dbData) {
        List<Object> list = null;

        if (StringUtils.isNotEmpty(dbData)) {
            list = JSON.parseArray(dbData);
        }

        return list;
    }
}
