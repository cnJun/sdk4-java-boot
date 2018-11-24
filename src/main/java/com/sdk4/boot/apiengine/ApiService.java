package com.sdk4.boot.apiengine;

/**
 * 对外提供 API 服务接口
 *
 * Created by sh on 2018/3/16.
 */
public interface ApiService {

    /**
     * 对外提供 API 的服务名
     *
     * @return
     */
    String method();

    /**
     * 是否需要登录
     *
     * @return
     */
    boolean requiredLogin();

    /**
     * API 服务接口
     *
     * @param rc
     * @return
     */
    ApiResponse call(RequestContent rc);

}
