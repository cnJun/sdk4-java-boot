package com.sdk4.boot.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户信息
 *
 * @author sh
 */
@Data
@Entity(name = "BootAdminUser")
@Table(name = "bcom_admin_user")
public class AdminUser extends BootAdminUser {
}
