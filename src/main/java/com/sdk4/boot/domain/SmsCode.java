package com.sdk4.boot.domain;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 短信验证码
 *
 * @author sh
 */
@Data
@Entity(name = "BootSmsCode")
@Table(name = "bcom_sms_code")
public class SmsCode {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;

    /**
     * 验证码类型
     */
    private String type;

    /**
     * 手机国际区号
     */
    private String phoneArea;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 下发的验证码
     */
    private String code;

    /**
     * 下发时间
     */
    private Date createTime;

    /**
     * 使用时间
     */
    private Date usedTime;

    /**
     * 短信渠道名称
     */
    private String channelName;

    /**
     * 短信渠道返回的消息id
     */
    private String msgId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态说明
     */
    private String statusDesc;
}
