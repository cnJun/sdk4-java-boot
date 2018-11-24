package com.sdk4.boot.db;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sdk4.common.util.MapUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;

/**
 * 查询条件
 *
 * @author sh
 */
@Slf4j
@Data
public class AbstractQueryCondition {
    /**
     * 页码，从 0 开始
     */
    public Integer pageIndex;
    public Integer pageSize;

    private static Map<String, String> fieldAliases = Maps.newHashMap();

    static {
        fieldAliases.put("page", "pageIndex");
        fieldAliases.put("limit", "pageSize");
        fieldAliases.put("page_index", "pageIndex");
        fieldAliases.put("page_size", "pageSize");
    }

    public AbstractQueryCondition(Map params) {
        if (MapUtils.isNotEmpty(params)) {
            Map params_ = params;
            // 参数名转换
            if (!params.containsKey("pageIndex") && !params.containsKey("pageSize")) {
                params_ = Maps.newHashMap();
                Iterator<Map.Entry> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = iterator.next();
                    Object key = entry.getKey();
                    Object keyAlias = fieldAliases.get(key);
                    if (keyAlias != null) {
                        key = keyAlias;
                    }
                    params_.put(key, entry.getValue());
                }
            }

            Class cls = getClass();

            try {
                MapUtils.toJavaBeanObject(params_, cls, this);
            } catch (Exception e) {
                log.error("map转javabean失败:{}:{}", cls.getName(), JSON.toJSONString(params), e);
            }
        }

        if (this.pageIndex == null || this.pageIndex < 0) {
            this.pageIndex = 0;
        } else {
            this.pageIndex--;
        }
        if (this.pageSize == null || this.pageSize < 1) {
            this.pageSize = 10;
        }
    }
}
