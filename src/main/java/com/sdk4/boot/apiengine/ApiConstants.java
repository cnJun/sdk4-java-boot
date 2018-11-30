package com.sdk4.boot.apiengine;

/**
 * Created by sh on 2018/4/20.
 */
public final class ApiConstants {
    private ApiConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    /**
     * 应用ID
     */
    public static final String APP_ID = "app_id";

    /**
     * 子商户应用ID
     */
    public static final String SUB_APP_ID = "sub_app_id";

    /**
     * 接口名称
     */
    public static final String METHOD = "method";

    /**
     * 签名类型
     */
    public static final String SIGN_TYPE = "sign_type";

    /**
     * 请求参数的签名串
     */
    public static final String SIGN = "sign";

    /**
     * 送请求时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * 调用接口版本，固定值：1.0
     */
    public static final String VERSION = "version";

    /**
     * 业务请求参数的集合，JSON格式
     */
    public static final String BIZ_CONTENT = "biz_content";

    public static final String TOKEN = "token";

    /**
     * 业务响应数据的集合，JSON格式
     */
    public static final String RSP_CONTENT = "rsp_content";
}
