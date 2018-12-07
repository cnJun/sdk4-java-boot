package com.sdk4.boot.fileupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;

import java.io.File;
import java.io.InputStream;

/**
 * 文件后缀工具类
 *
 * @author sh
 */
@Slf4j
public class FileSuffixUtils {
    private FileSuffixUtils() {
        throw new IllegalStateException("Utility class");
    }
    
    public static String getFileSuffix(File file) {
        try {
            String contentType = new Tika().detect(file);
            return getFileSuffix(contentType);
        } catch (Exception e) {
            log.error("get file suffix error", e);
        }

        return null;
    }

    public static String getFileSuffix(InputStream is) {
        try {
            String contentType = new Tika().detect(is);
            return getFileSuffix(contentType);
        } catch (Exception e) {
            log.error("get file suffix error", e);
        }

        return null;
    }

    public static String getFileSuffix(String contentType) {
        String suffix = null;

        try {
            if (StringUtils.isNotEmpty(contentType)) {
                MimeTypes types = MimeTypes.getDefaultMimeTypes();
                MimeType mime = types.forName(contentType);
                suffix = mime.getExtension();
                if (suffix != null && suffix.startsWith(".")) {
                    suffix = suffix.substring(1);
                }
            }
        } catch (Exception e) {
            log.error("get file suffix error", e);
        }

        return suffix;
    }

    /**
     * 获取文件的真实后缀名。目前只支持JPG, GIF, PNG, BMP四种图片文件。
     *
     * @param bytes 文件字节流
     * @return JPG, GIF, PNG or null
     */
    public static String getFileSuffix(byte[] bytes) {
        if (bytes == null || bytes.length < 10) {
            return null;
        }

        if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
            return "gif";
        } else if (bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
            return "png";
        } else if (bytes[6] == 'J' && bytes[7] == 'F' && bytes[8] == 'I' && bytes[9] == 'F') {
            return "jpg";
        } else if (bytes[0] == 'B' && bytes[1] == 'M') {
            return "bmp";
        } else {
            return null;
        }
    }

    /**
     * 获取文件的真实媒体类型。目前只支持JPG, GIF, PNG, BMP四种图片文件。
     *
     * @param bytes 文件字节流
     * @return 媒体类型(MEME-TYPE)
     */
    public static String getMimeType(byte[] bytes) {
        String suffix = getFileSuffix(bytes);
        String mimeType;

        if ("JPG".equals(suffix)) {
            mimeType = "image/jpeg";
        } else if ("GIF".equals(suffix)) {
            mimeType = "image/gif";
        } else if ("PNG".equals(suffix)) {
            mimeType = "image/png";
        } else if ("BMP".equals(suffix)) {
            mimeType = "image/bmp";
        } else {
            mimeType = "application/octet-stream";
        }

        return mimeType;
    }
}
