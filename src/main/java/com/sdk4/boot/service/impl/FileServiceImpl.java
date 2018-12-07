package com.sdk4.boot.service.impl;

import com.sdk4.boot.bo.LoginUser;
import com.sdk4.boot.common.BaseResponse;
import com.sdk4.boot.domain.File;
import com.sdk4.boot.exception.BaseError;
import com.sdk4.boot.fileupload.FileSaveInfo;
import com.sdk4.boot.fileupload.FileUploadInfo;
import com.sdk4.boot.repository.FileRepository;
import com.sdk4.boot.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author sh
 */
@Service("BootFileService")
public class FileServiceImpl implements FileService {
    @Autowired
    FileRepository fileRepository;

    @Override
    public BaseResponse<File> saveUploadFile(String type, FileUploadInfo uploadInfo, FileSaveInfo saveInfo, LoginUser loginUser) {
        BaseResponse<File> result = new BaseResponse<>();

        File file = new File();
        file.setType(type);
        file.setFileKey(uploadInfo.getFileKey());
        file.setOriginalFilename(uploadInfo.getOriginalFilename());
        file.setContentType(uploadInfo.getContentType());
        file.setSuffix(uploadInfo.getSuffix());
        file.setFileSize(uploadInfo.getFileSize());
        file.setUploadTime(new Date());
        file.setObjectKey(saveInfo.getObjectKey());
        file.setFileId(saveInfo.getFileId());
        if (loginUser != null) {
            file.setUserType(loginUser.getUserType().name());
            file.setUserId(loginUser.getUserId());
        }

        try {
            fileRepository.save(file);
            result.put(0, "文件信息保存成功", file);
        } catch (Exception e) {
            result.put(BaseError.BIZ_FAIL.getCode(), "文件信息保存失败", e);
        }

        return result;
    }

    @Override
    public File getByFileId(String fileId) {
        File where = new File();
        where.setFileId(fileId);

        List<File> list = fileRepository.findAll(Example.of(where));

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public BaseResponse<File> saveBizInfo(String fileId, String bizId) {
        BaseResponse<File> result = new BaseResponse<>();

        File file = getByFileId(fileId);
        if (file == null) {
            result.put(BaseError.BIZ_FAIL.getCode(), "文件id不存在");
        } else if (StringUtils.isNotEmpty(file.getBizLinkId())) {
            result.put(BaseError.BIZ_FAIL.getCode(), "文件已绑定业务id");
        } else {
            file.setBizLinkId(bizId);
            file.setBizLinkTime(new Date());
            try {
                fileRepository.save(file);
                result.put(0, "绑定业务id成功", file);
            } catch (Exception e) {
                result.put(BaseError.BIZ_FAIL.getCode(), "文件绑定业务id失败");
            }
        }

        return result;
    }
}
