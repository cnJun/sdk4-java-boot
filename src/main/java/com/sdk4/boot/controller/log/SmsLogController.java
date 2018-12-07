package com.sdk4.boot.controller.log;

import com.sdk4.boot.common.PageResponse;
import com.sdk4.boot.domain.SmsCode;
import com.sdk4.boot.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author sh
 */
@RestController("BootSmsLogController")
@RequestMapping("/ops")
public class SmsLogController {
    @Autowired
    LogService logService;

    @RequestMapping(value = { "smslog" }, produces = "application/json;charset=UTF-8" )
    public PageResponse<SmsCode> smslog(@RequestBody Map reqMap) {
        PageResponse<SmsCode> pageResponse = logService.querySmsCode(reqMap);
        return pageResponse;
    }
}
