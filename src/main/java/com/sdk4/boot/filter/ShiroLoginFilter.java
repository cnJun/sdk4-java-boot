package com.sdk4.boot.filter;

import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.CommonErrorCode;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sh
 */
public class ShiroLoginFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest req, ServletResponse rsp) throws IOException {
        HttpServletResponse response = (HttpServletResponse) rsp;

        AjaxResponse ret = new AjaxResponse(CommonErrorCode.NOT_LOGIN);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(ret.toJSONString());

        return false;
    }

}
