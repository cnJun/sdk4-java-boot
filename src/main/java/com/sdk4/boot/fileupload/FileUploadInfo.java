package com.sdk4.boot.fileupload;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传信息
 *
 * @author sh
 */
@Data
public class FileUploadInfo {

    private String fileKey;
    private String originalFilename;
    private String contentType;
    private String suffix;
    private long fileSize;
    @JSONField(serialize=false)
    private MultipartFile file;

}
