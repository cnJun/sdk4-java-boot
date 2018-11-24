package com.sdk4.boot;

import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.PasswordModeEnum;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author sh
 */
public class AjaxResponseTest {

    @Test
    public void testToJSONString() throws Exception {
        User user = new User();
        user.setMobile("13100000001");
        user.setPasswordMode(PasswordModeEnum.MD5);
        user.setLoginName("sh");
        user.setCreateTime(new Date());

        AjaxResponse ajaxResponse = new AjaxResponse(0, "SUCCESS", user);

        assertThat(ajaxResponse.getCode()).isEqualTo(0);
        assertThat(ajaxResponse.toJSONString()).contains("login_name");
        assertThat(ajaxResponse.toJSONString()).doesNotContain("loginName");
    }

    @Test
    public void testMapToJSONString() throws Exception {
        Map map = Collections.singletonMap("UserName", "shJ");
        AjaxResponse ajaxResponse = new AjaxResponse(0, "SUCCESS", map);

        assertThat(ajaxResponse.getCode()).isEqualTo(0);
        assertThat(ajaxResponse.toJSONString()).contains("UserName");
    }
}