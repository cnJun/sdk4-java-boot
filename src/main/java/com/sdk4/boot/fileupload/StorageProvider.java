package com.sdk4.boot.fileupload;

import lombok.Data;

/**
 * @author sh
 */
@Data
public class StorageProvider {
    private StorageMode mode;
    private String localPath;
}
