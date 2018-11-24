package com.sdk4.boot.apiengine;

/**
 * Created by sh on 2018/4/20.
 */
public interface ApiConstants {

    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";

    /**
     * 应用ID
     */
    String APP_ID = "app_id";

    /**
     * 子商户应用ID
     */
    String SUB_APP_ID = "sub_app_id";

    /**
     * 接口名称
     */
    String METHOD = "method";

    /**
     * 签名类型
     */
    String SIGN_TYPE = "sign_type";

    /**
     * 请求参数的签名串
     */
    String SIGN = "sign";

    /**
     * 送请求时间，格式：yyyy-MM-dd HH:mm:ss
     */
    String TIMESTAMP = "timestamp";

    /**
     * 调用接口版本，固定值：1.0
     */
    String VERSION = "version";

    /**
     * 业务请求参数的集合，JSON格式
     */
    String BIZ_CONTENT = "biz_content";

    String TOKEN = "token";

    /**
     * 业务响应数据的集合，JSON格式
     */
    String RSP_CONTENT = "rsp_content";
}
