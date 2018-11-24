package com.sdk4.boot.controller.log;

import com.google.common.collect.Lists;
import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.apiengine.ApiFactory;
import com.sdk4.boot.apiengine.ApiService;
import com.sdk4.boot.db.PageResult;
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

    @ResponseBody
    @RequestMapping(value = { "apinames" }, produces = "application/json;charset=UTF-8" )
    public String apinames(@RequestBody Map reqMap) {
        Map<String, ApiService> apis = ApiFactory.getAllApi();
        List<String> names = Lists.newArrayList();
        for (Map.Entry<String, ApiService> entry : apis.entrySet()) {
            names.add(entry.getValue().method());
        }

        AjaxResponse ret = new AjaxResponse(0, "获取成功", names);

        return ret.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = { "apilog" }, produces = "application/json;charset=UTF-8" )
    public String apilog(@RequestBody Map reqMap) {
        PageResult<ApiLog> pageResult = logService.queryApiLog(reqMap);

        AjaxResponse ret = AjaxResponse.by(pageResult);

        return ret.toJSONString();
    }
}
