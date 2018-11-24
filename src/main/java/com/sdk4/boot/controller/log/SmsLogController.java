package com.sdk4.boot.controller.log;

import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.db.PageResult;
import com.sdk4.boot.domain.SmsCode;
import com.sdk4.boot.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ResponseBody
    @RequestMapping(value = { "smslog" }, produces = "application/json;charset=UTF-8" )
    public String smslog(@RequestBody Map reqMap) {
        PageResult<SmsCode> pageResult = logService.querySmsCode(reqMap);

        AjaxResponse ret = AjaxResponse.by(pageResult);

        return ret.toJSONString();
    }
}
