package com.sdk4.boot.controller.log;

import com.google.common.collect.Lists;
import com.sdk4.boot.apiengine.ApiFactory;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.common.PageResponse;
import com.sdk4.boot.domain.ApiLog;
import com.sdk4.boot.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author sh
 */
@RestController("BootApiLogController")
@RequestMapping("/ops")
public class ApiLogController {

    @Autowired
    LogService logService;

    @RequestMapping(value = { "apinames" }, produces = "application/json;charset=UTF-8" )
    public BaseResponse<List<String>> apinames(@RequestBody Map reqMap) {
        Map<String, ApiService> apis = ApiFactory.getAllApi();
        List<String> names = Lists.newArrayList();
        for (Map.Entry<String, ApiService> entry : apis.entrySet()) {
            names.add(entry.getValue().method());
        }

        BaseResponse<List<String>> ret = new BaseResponse(0, "获取成功", names);

        return ret;
    }

    @RequestMapping(value = { "apilog" }, produces = "application/json;charset=UTF-8" )
    public PageResponse apilog(@RequestBody Map reqMap) {
        PageResponse<ApiLog> pageResponse = logService.queryApiLog(reqMap);
        return pageResponse;
    }
}
