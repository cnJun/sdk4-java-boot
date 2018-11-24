package com.sdk4.boot.apiengine;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**
 * API 请求参数
 *
 * @author sh
 */
@Data
public class ApiRequest {
    /**
     * 请求地址
     */
    private String requestUrl;

    /**
     * user-agent
     */
    private String userAgent;

    /**
     * 客户端请求 IP
     */
    private String clientIp;

    /**
     * 请求 HTTP 方法
     */
    private String httpMethod;

    /**
     * 请求 ContentType
     */
    private String contentType;

    /**
     * HTTP 头
     */
    private Map<String, String> headers;

    /**
     * 请求键值对参数
     */
    private Map<String, String> requestParameters;

    /**
     * HTTP 请求非 key-value 格式 body 部分
     */
    private JSONObject requestBodyObject;

    /**
     * 上传的文件
     */
    private Map<String, String> files;

    /**
     * 请求方法
     */
    private String method;
}
