package com.sdk4.boot.trace;

import com.sdk4.common.id.IdUtils;

/**
 * id 生成
 *
 * @author sh
 * @date 2018/12/21
 */
public class TraceIdGenerator {
    public static String generate() {
        return IdUtils.fastUUID().toString();
    }
}
