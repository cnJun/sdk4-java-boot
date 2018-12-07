package com.sdk4.boot.service;

import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.File;
import com.sdk4.boot.fileupload.FileSaveInfo;
import com.sdk4.boot.fileupload.FileUploadInfo;

/**
 * @author sh
 */
public interface FileService {

    BaseResponse<File> saveUploadFile(String type, FileUploadInfo uploadInfo, FileSaveInfo saveInfo, LoginUser loginUser);

    File getByFileId(String fileId);

    BaseResponse<File> saveBizInfo(String fileId, String bizId);

}
