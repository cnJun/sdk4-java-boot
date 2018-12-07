package com.sdk4.boot.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 文件
 *
 * @author sh
 */
@Data
@Entity(name = "BootFile")
@Table(name = "bcom_file")
public class File {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;
    private String type;
    private String fileKey;
    private String originalFilename;
    private String contentType;
    private String suffix;
    private Long fileSize;
    private Date uploadTime;
    private String objectKey;
    private String fileId;
    private String userType;
    private String userId;
    private String bizLinkId;
    private Date bizLinkTime;
}
