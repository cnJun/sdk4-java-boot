package com.sdk4.boot.domain;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * API 接口日志
 *
 * @author sh
 */
@Data
@Entity(name = "BootApiLog")
@Table(name = "bcom_api_log")
public class ApiLog {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;
    private String requestId;
    private String type;
    private String url;
    private String method;
    private String fromIp;
    private String headers;
    private String userId;
    private String userName;
    private String reqContent;
    private Date reqTime;
    private String rspContent;
    private Date rspTime;
    private Integer code;
    private String msg;
    private String exceptionString;
}
