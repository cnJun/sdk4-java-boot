package com.sdk4.boot.service;

import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.User;

/**
 * 用户接口
 *
 * @author sh
 */
public interface UserService {

    /**
     * 使用手机号码注册
     *
     * @param mobile
     * @param password
     * @return
     */
    BaseResponse<User> registerByMobile(String mobile, String password);

    /**
     * 用户登录
     *
     * @param mobile
     * @param password
     * @return
     */
    BaseResponse<User> loginByMobile(String mobile, String password);

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @return
     */
    User getUser(String id);

    /**
     * 根据手机号码获取用户
     *
     * @param mobile
     * @return
     */
    User getUserByMobile(String mobile);

}
