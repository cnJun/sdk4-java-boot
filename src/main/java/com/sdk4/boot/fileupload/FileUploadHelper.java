package com.sdk4.boot.fileupload;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.sdk4.common.id.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author sh
 */
@Slf4j
public class FileUploadHelper {
    private FileUploadHelper() {
        throw new IllegalStateException("Utility class");
    }

    private static final String FMT_FN = "yyyyMMdd/HH";

    private static FileUploadConfig fileUploadConfig;

    public static void loadConfig(FileUploadConfig config) {
        FileUploadHelper.fileUploadConfig = config;
    }

    public static FileTypeConfig getFileTypeConfig(String type) {
        return fileUploadConfig.getFiles().get(type);
    }

    public static StorageProvider getStorageProvider(String provider) {
        return fileUploadConfig.getProvider().get(provider);
    }

    public static List<FileUploadInfo> getFileUploadInfos(HttpServletRequest request) {
        List<FileUploadInfo> files = Lists.newArrayList();

        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            Iterator<String> filenames = multiRequest.getFileNames();
            while (filenames.hasNext()) {
                MultipartFile file = multiRequest.getFile(filenames.next());
                if (file != null) {
                    FileUploadInfo fileInfo = new FileUploadInfo();

                    fileInfo.setFileKey(file.getName());
                    fileInfo.setOriginalFilename(file.getOriginalFilename());
                    fileInfo.setContentType(file.getContentType());
                    fileInfo.setSuffix(FileSuffixUtils.getFileSuffix(file.getContentType()));
                    fileInfo.setFileSize(file.getSize());
                    fileInfo.setFile(file);

                    files.add(fileInfo);
                }
            }
        }

        return files;
    }

    public static FileSaveInfo saveFile(String type, FileUploadInfo fileInfo) {
        FileSaveInfo result = null;

        FileTypeConfig fileTypeConfig = getFileTypeConfig(type);
        StorageProvider storageProvider = fileTypeConfig == null ? null : getStorageProvider(fileTypeConfig.getProvider());

        if (fileTypeConfig != null && storageProvider != null) {
            if (StorageMode.local == storageProvider.getMode()) {
                String objectKey = getObjectKey(type);
                File path = new File(getPath(objectKey, storageProvider));
                if (!path.exists()) {
                    path.mkdirs();
                }
                String fileId = IdUtils.fastUUID().toString();
                try {
                    objectKey = objectKey + "/" + fileId + "." + fileInfo.getSuffix();
                    fileInfo.getFile().transferTo(new File(path, fileId));
                    result = new FileSaveInfo(objectKey, fileId, getFileUrl(objectKey));
                } catch (IOException e) {
                    log.error("上传文件保存失败:{}", JSON.toJSONString(fileInfo), e);
                }
            }
        }

        return result;
    }

    public static File readFile(String type, String objectKey) {
        FileTypeConfig fileTypeConfig = getFileTypeConfig(type);
        StorageProvider storageProvider = fileTypeConfig == null ? null : getStorageProvider(fileTypeConfig.getProvider());

        if (fileTypeConfig != null && storageProvider != null) {
            return new File(getPath(objectKey, storageProvider));
        }

        return null;
    }

    public static String getFileUrl(String objectKey) {
        StringBuilder url = new StringBuilder(fileUploadConfig.getAccessBaseUrl());
        if (!fileUploadConfig.getAccessBaseUrl().endsWith("/")) {
            url.append('/');
        }
        url.append(objectKey);
        return url.toString();
    }

    private static String getPath(String objectKey, StorageProvider storageProvider) {
        StringBuilder path = new StringBuilder();
        path.append(storageProvider.getLocalPath());
        if (!storageProvider.getLocalPath().endsWith(File.separator)) {
            path.append(File.separator);
        }
        path.append(objectKey);
        return path.toString();
    }

    private static String getObjectKey(String type) {
        StringBuilder objectKey = new StringBuilder();
        objectKey.append(type);
        objectKey.append("/");
        objectKey.append(new DateTime().toString(FMT_FN));
        return objectKey.toString();
    }
}
