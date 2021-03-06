package com.sdk4.boot.service;

import com.sdk4.boot.common.PageResponse;
import com.sdk4.boot.domain.ApiLog;
import com.sdk4.boot.domain.SmsCode;

import java.util.Map;

/**
 * 日志服务
 *
 * @author sh
 */
public interface LogService {
    /**
     * 查询api日志列表
     *
     * @param params
     * @return
     */
    PageResponse<ApiLog> queryApiLog(Map params);

    /**
     * 查询短信验证码发送列表
     *
     * @param params
     * @return
     */
    PageResponse<SmsCode> querySmsCode(Map params);

}
