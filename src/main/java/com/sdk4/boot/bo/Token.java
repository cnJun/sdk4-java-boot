package com.sdk4.boot.bo;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.common.util.JWTUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

/**
 * 授权认证 Token 结构
 *
 * @author sh
 */
@Slf4j
@Data
public class Token {
    private UserTypeEnum type;
    private String login_id;
    private Date expireTime;
    private Map<String, Object> extra;

    public boolean expire() {
        boolean result = false;

        if (expireTime != null && expireTime.getTime() < System.currentTimeMillis()) {
            result = true;
        }

        return result;
    }

    public String tokenString() throws UnsupportedEncodingException {
        String jsonString = JSON.toJSONString(this);
        return JWTUtils.createJsonToken(jsonString);
    }

    public static Token create(LoginUser loginUser) {
        Token token = new Token();

        token.setType(loginUser.getUserType());
        token.setLogin_id(loginUser.getLoginId());
        token.setExpireTime(loginUser.getExpireTime());

        return token;
    }

    public static Token parseToken(String tokenString) {
        Token token = null;

        try {
            String jsonString = JWTUtils.verifyJsonToken(tokenString);
            if (StringUtils.isNotEmpty(jsonString)) {
                token = JSON.parseObject(jsonString, Token.class);
            }
        } catch (Exception e) {
            log.error("解析token失败:{}", tokenString, e);
        }

        return token;
    }
}
