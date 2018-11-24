package com.sdk4.boot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sdk4.boot.apiengine.ApiConstants;
import com.sdk4.boot.apiengine.ApiExecutor;
import com.sdk4.boot.apiengine.ApiRequest;
import com.sdk4.boot.apiengine.ApiResponse;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.domain.ApiLog;
import com.sdk4.boot.domain.User;
import com.sdk4.boot.enums.UserTypeEnum;
import com.sdk4.boot.repository.ApiLogRepository;
import com.sdk4.common.base.ExceptionUtils;
import com.sdk4.common.id.IdUtils;
import com.sdk4.common.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * API 接口入口
 *
 * @author sh
 */
@Slf4j
@RestController("BootApiController")
@RequestMapping("")
public class ApiController {

    @Autowired
    ApiExecutor apiExecutor;

    @Autowired
    ApiLogRepository apiLogRepository;

    @Value("${sdk4.api.apilog}")
    private String apilogMode;

    @ResponseBody
    @RequestMapping(value = { "gateway.do", "services/**" }, produces = "application/json;charset=UTF-8" )
    public String gateway(HttpServletRequest request, HttpServletResponse response) {
        ApiRequest apiReq = new ApiRequest();

        String requestId = IdUtils.fastUUID().toString();

        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            url = url + (StringUtils.isEmpty(queryString) ? "" : "?" + queryString);
        }
        String httpMethod = request.getMethod();
        String contentType = request.getContentType();

        String ip = WebUtils.getRemoteAddress(request);

        Map<String, String> headers = WebUtils.getHeaders(request);

        ApiLog apiLog = new ApiLog();
        apiLog.setReqTime(new Date());
        apiLog.setType(httpMethod);
        apiLog.setUrl(url);
        apiLog.setFromIp(ip);
        apiLog.setHeaders(JSON.toJSONString(headers));

        Map<String, String> reqParams = Maps.newHashMap();

        String bodyString = null;
        JSONObject bodyObject = null;

        ApiResponse apiRsp = null;

        // body json
        if (StringUtils.isNotEmpty(contentType) && contentType.toLowerCase().contains("application/json")) {
            try {
                bodyString = WebUtils.getStreamAsString(request.getInputStream(), "UTF-8");
            } catch (IOException e) {
                log.error("读取 API 请求流时异常", e);

                apiRsp = new ApiResponse(4, "请求数据读取失败", e);
            }
            if (StringUtils.isNotEmpty(bodyString)) {
                try {
                    bodyObject = JSON.parseObject(bodyString);
                } catch (Exception e) {
                    log.error("HTTP请求body部分不是一个有效的JSON:{}", bodyString, e);

                    apiRsp = new ApiResponse(4, "请求body部分不是一个有效的JSON串", e);
                }
            }
        }

        // key-value parameter
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String val = request.getParameter(name);
            reqParams.put(name, val);
        }

        // files
        /**
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> filenames = multiRequest.getFileNames();
            while (filenames.hasNext()) {
                MultipartFile file = multiRequest.getFile(filenames.next());
                if (file != null) {
                    try {
                        file.transferTo(new File("/Users/9dsoft/Downloads/aa.png"));
                    } catch (IOException e) {
                        e.printStackTrace();

                        log.error("上传文件读取错误:{}", file.getName(), e);
                    }
                }
            }
        }*/

        String method = null;
        if (bodyObject != null) {
            method = bodyObject.getString(ApiConstants.METHOD);
        }
        if (StringUtils.isEmpty(method) && uri.startsWith("/services/")) {
            String urlMethod = uri.substring(10);
            method = urlMethod.replaceAll("/", ".");
        }

        if (apiRsp == null) {
            if (StringUtils.isEmpty(method)) {
                apiRsp = new ApiResponse(4, "缺少必选参数method");
            } else {
                apiReq.setMethod(method);
                apiReq.setRequestUrl(url);
                apiReq.setUserAgent(request.getHeader("user-agent"));
                apiReq.setClientIp(ip);
                apiReq.setHttpMethod(httpMethod);
                apiReq.setContentType(contentType);
                apiReq.setHeaders(headers);
                apiReq.setRequestParameters(reqParams);
                apiReq.setRequestBodyObject(bodyObject);
                apiReq.setFiles(null);

                StringBuilder reqConent = new StringBuilder();

                if (reqParams.size() > 0) {
                    reqConent.append(JSON.toJSONString(reqParams));
                }

                if (StringUtils.isNotEmpty(bodyString)) {
                    if (reqConent.length() > 0) {
                        reqConent.append("\n\nBODY:");
                    }
                    reqConent.append(bodyString);
                }
                apiLog.setReqContent(reqConent.toString());

                // call service
                try {
                    apiRsp = apiExecutor.call(apiReq);
                } catch (Exception e) {
                    e.printStackTrace();

                    apiRsp = new ApiResponse(5, "系统异常", e);

                    log.error("系统异常", e);
                }
                apiRsp.setRequest_id(requestId);

                apiLog.setMethod(method);
                apiLog.setRequestId(requestId);
                apiLog.setRspTime(new Date());
                apiLog.setCode(apiRsp.getCode());
                apiLog.setMsg(apiRsp.getMsg());
            }
        }

        LoginUser loginUser = apiRsp.getLoginUser();
        if (loginUser != null) {
            apiLog.setUserId(loginUser.getUserId());
            if (UserTypeEnum.COMMON_USER == loginUser.getUserType()) {
                User user = loginUser.toUserObject();
                apiLog.setUserName(user == null ? null : user.getMobile());
            }
        }

        String retJSONString = apiRsp.toJSONString();

        apiLog.setRspContent(retJSONString);

        if (StringUtils.equals(apilogMode, "db")) {
            if (apiRsp.getException() != null) {
                apiLog.setExceptionString(ExceptionUtils.toString(apiRsp.getException()));
            }

            try {
                apiLogRepository.save(apiLog);
            } catch (Exception e) {
                log.error("API调用日志保存失败:{}", JSON.toJSONString(apiLog), e);
            }
        } else {
            log.info("API调用:{}:{}", apiLog.getMethod(), JSON.toJSONString(apiLog), apiRsp.getException());
        }

        return retJSONString;
    }
}
