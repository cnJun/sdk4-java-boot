package com.sdk4.boot.service;

import com.sdk4.boot.domain.SysConfig;

import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * @author sh
 */
public interface SysConfigService {

    SysConfig save(String name, String value);

    SysConfig get(String name);

    Map<String, SysConfig> get(List<String> nameList);

}
