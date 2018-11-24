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
@Entity(name = "BootUser")
@Table(name = "bcom_user")
public class User extends BootUser {
}
