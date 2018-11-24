package com.sdk4.boot.domain;

import com.sdk4.boot.enums.PasswordModeEnum;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理用户
 *
 * @author sh
 */
@Data
@MappedSuperclass
public class BootAdminUser implements Serializable {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 密码加密模式
     */
    @Enumerated(EnumType.STRING)
    private PasswordModeEnum passwordMode;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String description;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}
