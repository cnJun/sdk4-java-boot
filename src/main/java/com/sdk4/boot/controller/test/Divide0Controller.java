package com.sdk4.boot.controller.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 除0错误
 *
 * @author sh
 */
@RestController
@RequestMapping("test")
public class Divide0Controller {

    @ResponseBody
    @RequestMapping("divide0")
    public String divide0() {
        int n = 1 / 0;
        return "divide 0 error";
    }

}
