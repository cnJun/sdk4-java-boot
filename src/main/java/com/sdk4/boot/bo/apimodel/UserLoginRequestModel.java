package com.sdk4.boot.bo.apimodel;

import lombok.Data;

/**
 * 用户登录请求
 *
 * @author sh
 */
@Data
public class UserLoginRequestModel {
    private String mobile;
    private String password;
}
