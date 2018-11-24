package com.sdk4.boot.controller.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sdk4.boot.AjaxResponse;
import com.sdk4.boot.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
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

    @ResponseBody
    @RequestMapping(value = { "upload" }, produces = "application/json;charset=utf-8")
    public String upload(HttpServletRequest request, HttpServletResponse response) {
        AjaxResponse ret = new AjaxResponse();

        /**
         * 业务类型
         */
        String type = request.getParameter("type");

        /**
         * base64格式的图片
         */
        String base64image = request.getParameter("base64image");

        int count = 0;
        List<Map<String, Object>> items = Lists.newArrayList();

        String lastError = "";

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> filenames = multiRequest.getFileNames();
            if (!filenames.hasNext()) {
                lastError = "未选择要上传的文件";
            } else {
                while (filenames.hasNext()) {
                    MultipartFile file = multiRequest.getFile(filenames.next());
                    if (file != null) {
                        Map<String, Object> item = Maps.newHashMap();

                        item.put("name", file.getName());
                        item.put("content_type", file.getContentType());
                        item.put("file_size", file.getSize());

                        /**
                        try {
                            String fileid = fileUploadService.save(type, mfile);

                            count++;

                            item.put("file_id", fileid);
                            item.put("url", fileUploadService.url(fileid, null));
                        } catch (Exception e) {
                            e.printStackTrace();

                            log.error("上传文件保存错误:{}", mfile.getName(), e);

                            lastError = e.getMessage();

                            item.put("error", lastError);
                        }*/

                        items.add(item);
                    }
                }
            }
        } else {
            lastError = "文件上传格式不对";
        }

        if (count > 0) {
            ret.putError(0, "上传" + items.size() + "个文件，成功" + count + "个");

            ret.put("type", type);
            ret.put("success_count", count);
            ret.put("files", items);
        } else {
            ret.putError(CommonErrorCode.BIZ_FAIL.getCode(), StringUtils.isNotEmpty(lastError) ? lastError : "上传文件失败");
            ret.put("files", items);
        }

        return ret.toJSONString();
    }
}
