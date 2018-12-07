package com.sdk4.boot.fileupload;

import lombok.Data;

import java.util.Map;

/**
 * 文件上传配置
 *
 * @author sh
 */
@Data
public class FileUploadConfig {
    private String accessBaseUrl;
    private Map<String, FileTypeConfig> files;
    private Map<String, StorageProvider> provider;
}
