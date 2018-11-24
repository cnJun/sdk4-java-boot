package com.sdk4.boot.apiengine;

/**
 * API 业务入口
 */
public interface ApiExecutor {

    /**
     * API调用
     *
     * @param apiRequest
     * @return
     */
    ApiResponse call(ApiRequest apiRequest);

}
