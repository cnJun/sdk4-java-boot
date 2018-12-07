package com.sdk4.boot.apiengine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.exception.BaseError;
import com.sdk4.boot.exception.DaoException;
import com.sdk4.boot.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * API 接口分发调用
 */
@Slf4j
@Service
public class DefaultApiExecutor implements ApiExecutor {

    @Autowired
    AuthService authService;

    @Override
    public ApiResponse call(ApiRequest req) {
        ApiResponse result = null;

        Map<String, String> params = req.getRequestParameters();

        // - body json 处理
        if (req.getRequestBodyObject() != null) {
            JSONObject bodyObject = req.getRequestBodyObject();
            for (Map.Entry<String, Object> entry : bodyObject.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (val != null) {
                    params.put(key, val.toString());
                }
            }
        }

        String method = req.getMethod();
        ApiService service = ApiFactory.getApi(method);
        RequestContent rc = new RequestContent(req, params);

        // 是否已登录 token
        String token = "";
        String authorization = req.getHeaders().get("authorization");
        if (StringUtils.isNotEmpty(authorization) && authorization.startsWith("Token ")) {
            token = authorization.substring(6);
        }
        if (StringUtils.isEmpty(token)) {
            token = params.get(ApiConstants.TOKEN);
        }

        LoginUser loginUser = null;
        if (StringUtils.isNotEmpty(token)) {
            loginUser = authService.getLoginUserByToken(token);
        }

        // - 调用服务
        if (service == null) {
            result = new ApiResponse(4, "方法" + method + "不存在");
        } else if (service.requiredLogin() && loginUser == null) {
            result = new ApiResponse(BaseError.NOT_LOGIN);
        } else {
            rc.setLoginUser(loginUser);

            try {
                result = service.call(rc);
            } catch (DaoException e) {
                log.error("调用方法发生异常:{}:{}", method, JSON.toJSONString(req), e);

                if (StringUtils.isNotEmpty(e.getErrorMessage())) {
                    result = new ApiResponse(BaseError.BIZ_FAIL.getCode(), e.getErrorMessage(), e);
                } else {
                    result = new ApiResponse(BaseError.SYSTEM_ERROR, e);
                }
            } catch (Exception e) {
                log.error("调用方法发生异常:{}:{}", method, JSON.toJSONString(req), e);

                result = new ApiResponse(BaseError.SYSTEM_ERROR);
                result.setException(e);
            }
        }

        result.setLoginUser(loginUser);

        return result;
    }

}
