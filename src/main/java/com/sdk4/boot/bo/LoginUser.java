package com.sdk4.boot.bo;

import com.sdk4.boot.domain.LoginToken;
import com.sdk4.boot.enums.UserTypeEnum;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * 登录用户信息
 *
 * @author sh
 */
@Data
public class LoginUser {
    /**
     * 登录id
     */
    private String loginId;

    /**
     * 发放的token
     */
    private Token token;

    /**
     * token 失效
     */
    private Date expireTime;

    /**
     * 扩展信息
     */
    private Map<String, Object> extra;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户对象
     */
    private Object userObject;

    public <T> T toUserObject() {
        T result = null;

        if (userObject != null) {
            try {
                result = (T) userObject;
            } catch (Exception e) {
            }
        }

        return result;
    }

    public static LoginUser by(Object userObject, LoginToken loginToken) throws UnsupportedEncodingException {
        LoginUser loginUser = new LoginUser();

        loginUser.setLoginId(loginToken.getId());
        loginUser.setExpireTime(loginToken.getExpireTime());
        loginUser.setUserType(loginToken.getType());
        loginUser.setUserId(loginToken.getUserId());
        loginUser.setUserObject(userObject);
        loginUser.setToken(Token.create(loginUser));

        return loginUser;
    }
}
