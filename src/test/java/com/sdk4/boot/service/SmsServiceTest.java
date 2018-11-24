package com.sdk4.boot.service;

import com.alibaba.fastjson.JSON;
import com.sdk4.boot.CallResult;
import com.sdk4.boot.Sdk4BootApplication;
import com.sdk4.boot.domain.SmsCode;
import com.sdk4.sms.SmsHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 短信测试
 *
 * @author sh
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Sdk4BootApplication.class)
public class SmsServiceTest {

    @Autowired
    SmsService smsService;

    @Test
    public void testSend() {
    }

    @Test
    public void testCheck() {
    }
}
