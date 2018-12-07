package com.sdk4.boot.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统配置
 *
 * @author sh
 */
@Data
@Entity(name = "BootSysConfig")
@Table(name = "bcom_sys_config")
public class SysConfig {
    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid.hex", parameters = {
            @org.hibernate.annotations.Parameter(name = "type", value = "string") })
    private String id;
    private String type;
    private String name;
    private String value;
    private String description;
}
