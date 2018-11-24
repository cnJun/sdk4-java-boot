package com.sdk4.boot.bo.apimodel;

import lombok.Data;

/**
 * 用户登录响应
 *
 * @author sh
 */
@Data
public class UserLoginResponseModel {
    private String id;
    private String mobile;
    private String token;
}
