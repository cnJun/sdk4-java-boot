package com.sdk4.boot.service;

import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.AdminUser;

/**
 * 管理用户接口
 *
 * @author sh
 */
public interface AdminUserService {

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @return
     */
    BaseResponse<AdminUser> loginByMobile(String mobile, String password);

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @return
     */
    AdminUser getAdminUser(String id);

}
