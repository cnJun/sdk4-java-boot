package com.sdk4.boot.service;

import com.sdk4.boot.Sdk4BootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author sh
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Sdk4BootApplication.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testRegUserByMobile() throws IOException {
    }

}
