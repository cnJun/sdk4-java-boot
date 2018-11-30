package com.sdk4.boot.apiengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * API 接口加载
 *
 * @author sh
 */
@Slf4j
@Component
public class ApiFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static ConcurrentMap<String, ApiService> apiService = new ConcurrentHashMap<>();

    public static ApiService getApi(String method) {
        return apiService.get(method);
    }

    public static Map<String, ApiService> getAllApi() {
        return apiService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        synchronized (ApiFactory.class) {
            ApiFactory.applicationContext = applicationContext;

            String[] apiBeanNames = applicationContext.getBeanNamesForType(ApiService.class);

            log.info("===================== Load API Service Begin =====================");

            int index = 0;

            for (String name : apiBeanNames) {
                ApiService service = getBean(name);

                String method = service.method();
                String methodClassName = service.getClass().getName();

                ApiService cache = apiService.get(method);
                if (cache != null) {
                    Class cacheClass = cache.getClass();
                    if (cacheClass.getPackage().getName().startsWith("com.sdk4.boot.")) {
                        log.info("load api service ({}){}:{}, override the class {}",
                                ++index, method, methodClassName,
                                cacheClass.getName());

                        apiService.put(service.method(), service);
                    } else {
                        log.info("don't load api service ({}){}:{}, has loaded the class {}",
                                ++index, method, methodClassName,
                                cacheClass.getName());
                    }
                } else {
                    apiService.put(service.method(), service);

                    log.info("load api service ({}){}:{}", ++index, method, methodClassName);
                }
            }

            log.info("===================== Load API Service End =====================");
        }

    }

    @SuppressWarnings("unchecked")
    private static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }
}
