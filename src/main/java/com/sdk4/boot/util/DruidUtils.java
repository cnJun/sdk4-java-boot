package com.sdk4.boot.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sh
 */
public class DruidUtils {
    public static void setLoginedName(HttpServletRequest request, String username) {
        if (username == null || username.trim().isEmpty()) {
            request.getSession().removeAttribute("druid-user");
        } else {
            request.getSession().setAttribute("druid-user", username);
        }
    }
}
