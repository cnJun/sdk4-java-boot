package com.sdk4.boot.fileupload;

import lombok.Data;

/**
 * 文件保存信息
 *
 * @author sh
 */
@Data
public class FileSaveInfo {

    private String objectKey;
    private String fileId;
    private String url;

    public FileSaveInfo(String objectKey, String fileId, String url) {
        this.objectKey = objectKey;
        this.fileId = fileId;
        this.url = url;
    }

}
