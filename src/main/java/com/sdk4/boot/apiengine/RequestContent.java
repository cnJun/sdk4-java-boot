package com.sdk4.boot.apiengine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.common.util.DateUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * Created by sh on 2018/6/19.
 */
@Data
public class RequestContent {
    /**
     * 应用ID
     */
    private String appId;

    /**
     * 接口名称
     */
    private String method;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 请求参数的签名串
     */
    private String sign;

    /**
     * 送请求时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String timestamp;

    /**
     * 调用接口版本，固定值：1.0
     */
    private String version;

    /**
     * 业务请求参数的集合，JSON格式字符串
     */
    private String bizContent;

    /**
     * 业务请求参数的集合，JSON格式对象
     */
    private JSONObject bizContentObject;

    /**
     * API 请求对象
     */
    private ApiRequest apiRequest;

    /**
     * 当前登录用户
     */
    private LoginUser loginUser;

    public RequestContent(ApiRequest apiRequest, Map<String, String> params) {
        this.apiRequest = apiRequest;

        this.appId = params.get(ApiConstants.APP_ID);
        this.method = params.get(ApiConstants.METHOD);
        this.signType = params.get(ApiConstants.SIGN_TYPE);
        this.sign = params.get(ApiConstants.SIGN);
        this.timestamp = params.get(ApiConstants.TIMESTAMP);
        this.version = params.get(ApiConstants.VERSION);
        this.bizContent = params.get(ApiConstants.BIZ_CONTENT);
        if (StringUtils.isNotEmpty(this.bizContent)) {
            this.bizContentObject = JSON.parseObject(this.bizContent);
        } else {
            this.bizContentObject = new JSONObject();
        }
    }

    public int optInt(String key, int defaultValue) {
        int result = defaultValue;

        if (this.bizContentObject.containsKey(key)) {
            result = this.bizContentObject.getInteger(key);
        }

        return result;
    }

    public Integer optInteger(String key, Integer defaultValue) {
        Integer result = defaultValue;

        if (this.bizContentObject.containsKey(key)) {
            result = this.bizContentObject.getInteger(key);
        }

        return result;
    }

    public String optString(String key, String defaultValue) {
        String result = defaultValue;

        if (this.bizContentObject.containsKey(key)) {
            result = this.bizContentObject.getString(key);
        }

        return result;
    }

    public Date optDate(String key, Date defaultValue) {
        Date result = defaultValue;

        if (this.bizContentObject.containsKey(key)) {
            String str = this.bizContentObject.getString(key);
            try {
                result = DateUtils.parseDate(str);
            } catch (Exception e) {
            }
        }

        return result;
    }

    public <T> T toJavaObject(Class<T> cls) {
        T result = null;
        if (this.bizContentObject.size() > 0) {
            result =  this.bizContentObject.toJavaObject(cls);
        }
        return result;
    }
}
