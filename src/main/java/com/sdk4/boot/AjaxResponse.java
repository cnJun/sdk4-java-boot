package com.sdk4.boot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.sdk4.boot.config.FastJsonConfigration;
import com.sdk4.boot.db.PageResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author sh
 */
@NoArgsConstructor
@Data
public class AjaxResponse {
    private int code;
    private String message;
    private Object data;

    @JSONField(serialize=false)
    protected Map<String, Object> tempDataMap = Maps.newTreeMap();

    public boolean success() {
        return code == 0;
    }

    public AjaxResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AjaxResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public AjaxResponse(CommonErrorCode error) {
        this.code = error.getCode();
        this.message = error.getMsg();
    }

    public void putError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void putError(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void putError(CommonErrorCode error) {
        this.code = error.getCode();
        this.message = error.getMsg();
    }

    public void put(String key, Object value) {
        this.tempDataMap.put(key, value);
    }

    public static AjaxResponse by(CallResult callResult) {
        return new AjaxResponse(callResult.getCode(), callResult.getMessage(), callResult.getData());
    }

    public static AjaxResponse by(PageResult pageResult) {
        AjaxResponse result = new AjaxResponse();

        result.putError(0, "查询成功");

        result.put("total", pageResult.getTotal());
        result.put("pageIndex", pageResult.getPageIndex());
        result.put("pageSize", pageResult.getPageSize());
        result.put("items", pageResult.getData());

        return result;
    }

    public String toJSONString() {
        if (StringUtils.isEmpty(message)) {
            message = "SUCCESS";
        }

        if (this.data == null && this.tempDataMap.size() > 0) {
            this.data = this.tempDataMap;
        }

        return JSON.toJSONString(this,
                FastJsonConfigration.serializeConfig,
                SerializerFeature.WriteDateUseDateFormat);
    }
}
