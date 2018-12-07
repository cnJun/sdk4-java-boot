package com.sdk4.boot.fileupload;

import lombok.Data;

import java.util.List;

/**
 * 文件类型配置
 *
 * @author sh
 */
@Data
public class FileTypeConfig {
    private String provider;
    private Long maxSize;
    private List<String> ext;
    private Boolean loginRequired;
}
