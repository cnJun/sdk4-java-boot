package com.sdk4.boot.apiengine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Maps;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.exception.BaseError;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * API 接口响应数据
 *
 * @author sh
 */
@NoArgsConstructor
@Data
public class ApiResponse {
    /**
     * 响应代码
     */
    protected Integer code;

    /**
     * 响应消息
     */
    protected String msg;

    /**
     * 请求ID
     */
    protected String request_id;

    /**
     * 业务响应数据的集合，JSON格式字符串或者Map对象格式
     */
    protected Object rsp_content;

    /**
     * rsp_content 返回数据临时存放
     */
    @JSONField(serialize=false)
    protected Map<String, Object> tempRspContentMap = Maps.newTreeMap();

    /**
     * 调用异常发生信息
     */
    @JSONField(serialize=false)
    protected Exception exception;

    /**
     * 当前用户
     */
    @JSONField(serialize=false)
    protected LoginUser loginUser;

    public ApiResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResponse(int code, String msg, Exception e) {
        this.code = code;
        this.msg = msg;
        this.exception = e;
    }

    public ApiResponse(BaseError error) {
        this.code = error.getCode();
        this.msg = error.getMessage();
    }

    public ApiResponse(BaseError error, Exception e) {
        this.code = error.getCode();
        this.msg = error.getMessage();
        this.exception = e;
    }

    public void put(String key, Object value) {
        this.tempRspContentMap.put(key, value);
    }

    public String toJSONString() {
        if (this.code == null) {
            this.code = 0;
            this.msg = ApiConstants.SUCCESS;
        }

        if (this.rsp_content == null && this.tempRspContentMap.size() > 0) {
            this.rsp_content = this.tempRspContentMap;
        }

        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss");
    }
}
