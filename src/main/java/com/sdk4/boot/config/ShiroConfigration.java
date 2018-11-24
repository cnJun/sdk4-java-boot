package com.sdk4.boot.config;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.sdk4.boot.filter.ShiroLoginFilter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro 权限配置
 *
 * @author sh
 */
@Slf4j
@Configuration
public class ShiroConfigration {
    @Bean
    @ConfigurationProperties(prefix = "spring.shiro")
    public ShiroConfig shiroConfig() {
        return new ShiroConfig();
    }

    public ShiroLoginFilter shiroLoginFilter() {
        return new ShiroLoginFilter();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "myShiroRealm")
    public MyShiroRealm myShiroRealm(){
        return new MyShiroRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(new MemoryConstrainedCacheManager());
        return securityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager, ShiroConfig shiroConfig) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        if (shiroConfig != null) {
            log.info("load shiro config: {}", JSON.toJSONString(shiroConfig));

            String authc = "authc";

            if (StringUtils.isNotEmpty(shiroConfig.loginUrl)) {
                shiroFilterFactoryBean.setLoginUrl(shiroConfig.loginUrl);
            } else {
                authc = "shiroLoginFilter";
            }
            if (StringUtils.isNotEmpty(shiroConfig.successUrl)) {
                shiroFilterFactoryBean.setSuccessUrl(shiroConfig.successUrl);
            }

            if (StringUtils.isNotEmpty(shiroConfig.unauthorizedUrl)) {
                shiroFilterFactoryBean.setUnauthorizedUrl(shiroConfig.unauthorizedUrl);
            }

            // 拦截器
            Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

            if (CollectionUtils.isNotEmpty(shiroConfig.anon)) {
                for (String item : shiroConfig.anon) {
                    filterChainDefinitionMap.put(item, "anon");
                }
            }
            if (CollectionUtils.isNotEmpty(shiroConfig.authc)) {
                for (String item : shiroConfig.authc) {
                    filterChainDefinitionMap.put(item, authc);
                }
            }
            shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

            // 自定义过滤器
            Map<String, Filter> filterMap = new LinkedHashMap<>();
            filterMap.put("shiroLoginFilter", shiroLoginFilter());
            shiroFilterFactoryBean.setFilters(filterMap);
        }

        return shiroFilterFactoryBean;
    }

    @Data
    public static class ShiroConfig {
        private List<String> anon;
        private List<String> authc;
        private String loginUrl;
        private String successUrl;
        private String unauthorizedUrl;
    }

}
