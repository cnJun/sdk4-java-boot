package com.sdk4.boot.service.impl;

import com.sdk4.boot.CallResult;
import com.sdk4.boot.domain.AdminUser;
import com.sdk4.boot.enums.PasswordModeEnum;
import com.sdk4.boot.repository.AdminUserRepository;
import com.sdk4.boot.service.AdminUserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sh
 */
@Service("BootAdminUserService")
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    AdminUserRepository adminUserRepository;

    @Override
    public CallResult<AdminUser> loginByMobile(String mobile, String password) {
        CallResult<AdminUser> result = new CallResult<>();

        AdminUser where = new AdminUser();
        where.setMobile(mobile);

        List<AdminUser> users = this.adminUserRepository.findAll(Example.of(where));
        if (CollectionUtils.isEmpty(users)) {
            result.setError(4, "用户不存在[" + mobile + "]");
        } else if (users.size() > 1) {
            result.setError(4, "用户重复[" + mobile + "]");
        } else {
            AdminUser user = users.get(0);

            if (StringUtils.isEmpty(user.getPassword())) {
                result.setError(4, "未设置登录密码");
            } else {
                String password_ = password;
                if (user.getPasswordMode() == PasswordModeEnum.MD5) {
                    password_ = DigestUtils.md5Hex(password);
                }
                if (StringUtils.equals(user.getPassword(), password_)) {
                    result.setError(0, "登录成功", user);
                } else {
                    result.setError(4, "密码不正确");
                }
            }
        }

        return result;
    }

    @Override
    public AdminUser getAdminUser(String id) {
        return adminUserRepository.findById(id).orElse(null);
    }
}
