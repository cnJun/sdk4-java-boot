package com.sdk4.boot.api.comm;

import com.sdk4.boot.apiengine.ApiConstants;
import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.apiengine.RequestContent;
import com.sdk4.boot.enums.ServiceNameEnum;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

/**
 * 服务器时间
 *
 * @author sh
 */
@Service
public class ServerTime implements ApiService {
    @Override
    public String method() {
        return ServiceNameEnum.ServerTime.getMethod();
    }

    @Override
    public boolean requiredLogin() {
        return false;
    }

    @Override
    public ApiResponse call(RequestContent rc) {
        ApiResponse ret = new ApiResponse(0, ApiConstants.SUCCESS);

        ret.put("now", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));

        return ret;
    }
}
