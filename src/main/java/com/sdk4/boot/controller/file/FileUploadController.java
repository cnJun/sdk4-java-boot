package com.sdk4.boot.controller.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.CallResult;
import com.sdk4.boot.CommonErrorCode;
import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.domain.File;
import com.sdk4.boot.fileupload.*;
import com.sdk4.boot.service.AuthService;
import com.sdk4.boot.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author sh
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileUploadController {

    @Autowired
    AuthService authService;

    @Autowired
    FileService fileService;

    @ResponseBody
    @RequestMapping(value = { "upload" }, produces = "application/json;charset=utf-8")
    public String upload(HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse ret = new AjaxResponse();

        String type = request.getParameter("type");
        if (StringUtils.isEmpty(type)) {
            ret.putError(CommonErrorCode.MISSING_PARAMETER.getCode(), "上传文件类型type不能为空");
        } else {
            FileTypeConfig ftc = FileUploadHelper.getFileTypeConfig(type);
            LoginUser loginUser = authService.getLoginUserFromSession();
            if (ftc == null) {
                ret.putError(CommonErrorCode.INVALID_PARAMETER.getCode(), "上传文件类型type不正确");
            } else if (ftc.getLoginRequired() && loginUser == null) {
                ret.putError(CommonErrorCode.NOT_LOGIN);
            } else {
                String base64image = request.getParameter("base64image");
                List<FileUploadInfo> files = FileUploadHelper.getFileUploadInfos(request);
                if (files.size() == 0) {
                    ret.putError(CommonErrorCode.BIZ_FAIL.getCode(), "未上传文件");
                } else {
                    List<Map<String, Object>> success = Lists.newArrayList();
                    List<Map<String, Object>> error = Lists.newArrayList();
                    for (FileUploadInfo info : files) {
                        if (ftc.getMaxSize() != null && ftc.getMaxSize() > 0 && info.getFileSize() > ftc.getMaxSize()) {
                            addError(error, info, "文件大小不能大于" + ftc.getMaxSize());
                        } else if (CollectionUtils.isNotEmpty(ftc.getExt()) && !ftc.getExt().contains(info.getSuffix())) {
                            addError(error, info, "文件类型只能上传" + StringUtils.join(ftc.getExt(), ",") + "类型的文件");
                        } else {
                            FileSaveInfo saveInfo = FileUploadHelper.saveFile(type, info);

                            if (saveInfo == null) {
                                addError(error, info, "文件上传失败");
                            } else {
                                CallResult<File> callResult = fileService.saveUploadFile(type, info, saveInfo, loginUser);

                                if (callResult.success()) {
                                    Map<String, Object> item = Maps.newHashMap();

                                    item.put("key", info.getFileKey());
                                    item.put("size", info.getFileSize());
                                    item.put("id", saveInfo.getFileId());
                                    item.put("url", saveInfo.getUrl());

                                    success.add(item);
                                } else {
                                    addError(error, info, "文件信息保存失败");
                                }
                            }
                        }
                    }

                    ret.put("type", type);
                    ret.put("success", success.size());
                    ret.put("fail", error.size());

                    if (error.size() > 0) {
                        ret.putError(CommonErrorCode.BIZ_FAIL.getCode(), "文件上传失败");
                        ret.put("errors", error);
                    } else {
                        ret.putError(0, "文件上传成功");
                        ret.put("files", success);
                    }
                }
            }
        }

        return ret.toJSONString();
    }

    private static int FILE_BASE_URL_LEN = "/file/resource/".length();

    @ResponseBody
    @RequestMapping(value = { "resource/**" })
    public String file(HttpServletRequest request, HttpServletResponse response) {
        String uri = request.getRequestURI();

        String fileUri = uri.substring(FILE_BASE_URL_LEN);
        int typeAt = fileUri.indexOf('/');
        int suffixAt = fileUri.lastIndexOf('.');
        if (typeAt > 0 && suffixAt > 0) {
            String type = fileUri.substring(0, typeAt);
            String suffix = fileUri.substring(suffixAt + 1);
            String objectKey = fileUri.substring(0, suffixAt);

            try {
                java.io.File file = FileUploadHelper.readFile(type, objectKey);
                if (file != null && file.exists()) {
                    Tika tika = new Tika();
                    String contentType = tika.detect(file);
                    String fileSuffix = "";
                    if (StringUtils.isEmpty(contentType)) {
                        response.setContentType("application/octet-stream");
                    } else {
                        response.setContentType(contentType);
                        fileSuffix = FileSuffixUtils.getFileSuffix(contentType);
                    }

                    if (false) {
                        response.setHeader("content-disposition",
                                "attachment;filename=" + URLEncoder.encode("filename.x", "UTF-8"));
                    }

                    OutputStream out = response.getOutputStream();
                    out.write(FileUtils.readFileToByteArray(file));
                    out.flush();
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    private void addError(List<Map<String, Object>> error, FileUploadInfo uploadInfo, String errorMessage) {
        Map<String, Object> item = Maps.newHashMap();

        item.put("key", uploadInfo.getFileKey());
        item.put("size", uploadInfo.getFileSize());
        item.put("error", errorMessage);

        error.add(item);
    }
}
