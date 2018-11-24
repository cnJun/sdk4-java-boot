package com.sdk4.boot.domain;

import com.sdk4.boot.enums.UserTypeEnum;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 权限认证 token
 *
 * Created by sh on 2018/4/25.
 */
@Data
@Entity(name = "BootLoginToken")
@Table(name = "bcom_login_token")
public class LoginToken {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;

    /**
     * 用户类型
     */
    @Enumerated(EnumType.STRING)
    private UserTypeEnum type;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 登录创建时间
     */
    private Date createTime;

    /**
     * 失效时间
     */
    private Date expireTime;


    public static LoginToken by(User user) {
        LoginToken loginToken = new LoginToken();

        loginToken.setType(UserTypeEnum.COMMON_USER);
        loginToken.setUserId(user.getId());
        loginToken.setCreateTime(new Date());

        return loginToken;
    }

    public static LoginToken by(AdminUser adminUser) {
        LoginToken loginToken = new LoginToken();

        loginToken.setType(UserTypeEnum.ADMIN_USER);
        loginToken.setUserId(adminUser.getId());
        loginToken.setCreateTime(new Date());

        return loginToken;
    }
}
