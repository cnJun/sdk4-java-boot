package com.sdk4.boot.domain;

import com.sdk4.boot.db.AbstractQueryCondition;
import com.sdk4.boot.enums.PasswordModeEnum;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户基本信息
 *
 * @author sh
 */
@Data
@MappedSuperclass
public class BootUser implements Serializable {
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
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 注册/创建时间
     */
    private Date createTime;

    /**
     * 用户状态
     */
    private Integer status;

    @Data
    public static class QueryCondition extends AbstractQueryCondition {
        public String mobile;
        public Integer status;

        public QueryCondition(Map params) {
            super(params);
        }
    }
}
