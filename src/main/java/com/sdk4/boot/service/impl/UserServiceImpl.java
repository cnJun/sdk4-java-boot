package com.sdk4.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.CallResult;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.PasswordModeEnum;
import com.sdk4.boot.enums.UserStatusEnum;
import com.sdk4.boot.repository.UserRepository;
import com.sdk4.boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户接口
 *
 * @author sh
 */
@Slf4j
@Service("BootUserService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public CallResult<User> registerByMobile(String mobile, String password) {
        CallResult<User> result = new CallResult<>();

        User where = new User();
        where.setMobile(mobile);

        if (this.userRepository.count(Example.of(where)) > 0) {
            result.setCode(4);
            result.setMessage("手机号码已注册[" + mobile + "]");
        } else {
            User user = new User();
            user.setMobile(mobile);
            if (StringUtils.isNotEmpty(password)) {
                user.setPasswordMode(PasswordModeEnum.MD5);
                user.setPassword(DigestUtils.md5Hex(password));
            }
            user.setStatus(UserStatusEnum.NORMAL.getCode());
            user.setCreateTime(new Date());

            try {
                this.userRepository.save(user);

                result.setCode(0);
                result.setMessage("注册成功");
                result.setData(user);
            } catch (Exception e) {
                log.error("用户注册保存失败:{}", JSON.toJSONString(user), e);

                result.setCode(4);
                result.setMessage("注册失败");
            }
        }

        return result;
    }

    @Override
    public CallResult<User> loginByMobile(String mobile, String password) {
        CallResult<User> result = new CallResult<>();

        User where = new User();
        where.setMobile(mobile);

        List<User> users = this.userRepository.findAll(Example.of(where));
        if (CollectionUtils.isEmpty(users)) {
            result.setError(4, "用户不存在[" + mobile + "]");
        } else if (users.size() > 1) {
            result.setError(4, "用户重复[" + mobile + "]");
        } else {
            User user = users.get(0);

            if (user.getStatus() != UserStatusEnum.NORMAL.getCode()) {
                result.setError(4, "登录失败，账号" + UserStatusEnum.of(user.getStatus()).getText());
            } else if (StringUtils.isEmpty(user.getPassword())) {
                result.setError(4, "未设置密码");
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
    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }
}
