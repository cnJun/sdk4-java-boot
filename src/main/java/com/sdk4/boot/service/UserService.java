package com.sdk4.boot.service;

import com.sdk4.boot.CallResult;
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
    CallResult<User> registerByMobile(String mobile, String password);

    /**
     * 用户登录
     *
     * @param mobile
     * @param password
     * @return
     */
    CallResult<User> loginByMobile(String mobile, String password);

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @return
     */
    User getUser(String id);

}
