package com.sdk4.boot.bo.apimodel;

import lombok.Data;

/**
 * 请求发送短信验证码
 *
 * @author sh
 */
@Data
public class SmsCodeRequestModel {
    private String type;
    private String mobile;
}
