package com.sdk4.boot.service;

import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.enums.UserTypeEnum;

/**
 * 用户权限
 *
 * @author sh
 */
public interface AuthService {

    /**
     * 登录
     */
    BaseResponse<LoginUser> loginByMobile(UserTypeEnum type, String mobile, String password);

    /**
     * 根据 token 获取用户
     *
     * @param tokenString
     * @return
     */
    LoginUser getLoginUserByToken(String tokenString);

    /**
     * 从会话中获取用户
     *
     * @return
     */
    LoginUser getLoginUserFromSession();

    /**
     * 退出登录
     *
     * @param loginId
     */
    void logoutByLoginId(String loginId);

}
