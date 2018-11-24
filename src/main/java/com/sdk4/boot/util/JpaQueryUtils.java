package com.sdk4.boot.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

/**
 * JPA 查询条件生成工具
 *
 * @author sh
 */
@Slf4j
public class JpaQueryUtils {

    private static List<String> pageIndexNames = Lists.newArrayList();
    private static List<String> pageSizeNames = Lists.newArrayList();

    static {
        pageIndexNames.add("page");
        pageIndexNames.add("page_index");
        pageIndexNames.add("pageIndex");

        pageSizeNames.add("limit");
        pageSizeNames.add("page_size");
        pageSizeNames.add("pageSize");
    }

    @Data
    public static class QueryCondition<T> {
        /**
         * 页序号，从0开始
         */
        private Integer pageIndex;

        /**
         * 页大小
         */
        private Integer pageSize;
        private Specification<T> specification;
        private Pageable pageable;
        private Sort sort;

        public Pageable defaultPageableIfEmpty(int pageIndex, int pageSize) {
            if (pageable == null) {
                if (sort == null) {
                    return PageRequest.of(pageIndex, pageSize);
                } else {
                    return PageRequest.of(pageIndex, pageSize, sort);
                }
            }
            return pageable;
        }
    }

    public static <T> QueryCondition<T> createQueryCondition(Class<T> cls, Map params, Sort.Order... orders) {
        QueryCondition<T> queryCondition = new QueryCondition<>();

        // 分页数据
        if (MapUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                String fieldName = k.toString();
                if (pageIndexNames.contains(fieldName)) {
                    queryCondition.pageIndex = Integer.parseInt(v.toString());
                } else if (pageSizeNames.contains(fieldName)) {
                    queryCondition.pageSize = Integer.parseInt(v.toString());
                }
            });
        }

        if (orders.length > 0) {
            queryCondition.sort = Sort.by(orders);
        }

        if (queryCondition.pageIndex != null) {
            queryCondition.pageSize = queryCondition.pageSize == null ? 10 : queryCondition.pageSize;
            if (queryCondition.sort == null) {
                queryCondition.pageable = PageRequest.of(queryCondition.pageIndex - 1, queryCondition.pageSize);
            } else {
                queryCondition.pageable = PageRequest.of(queryCondition.pageIndex - 1, queryCondition.pageSize, queryCondition.sort);
            }
        }

        // 查询条件
        Specification<T> spec = (Specification<T>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            if (MapUtils.isNotEmpty(params)) {
                BeanInfo beanInfo = null;
                PropertyDescriptor[] props = null;

                try {
                    beanInfo = Introspector.getBeanInfo(cls);
                    props = beanInfo.getPropertyDescriptors();
                } catch (IntrospectionException e) {
                    log.error("获取实体类属性失败:{}:{}", cls.getName(), JSON.toJSONString(params), e);
                }

                if (props != null) {
                    for (PropertyDescriptor prop : props) {
                        String fieldName = prop.getName();
                        Object fieldValue = params.get(fieldName);

                        // Map key 支持下划线连接的命名方式
                        if (fieldValue == null) {
                            String name2 = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
                            fieldValue = params.get(name2);
                        }

                        String strVal = fieldValue == null ? null : fieldValue.toString();

                        if (fieldValue == null || StringUtils.isEmpty(strVal)) {
                            continue;
                        }

                        Class fieldClass = prop.getPropertyType();
                        Class valueClass = fieldValue.getClass();

                        try {
                            if (valueClass.getInterfaces() != null
                                    && valueClass.getInterfaces().length > 0
                                    && valueClass.getInterfaces()[0].isAssignableFrom(List.class)) {
                                List list = (List) fieldValue;
                                if (list.size() > 0) {
                                    CriteriaBuilder.In inObj = criteriaBuilder.in(root.get(fieldName));
                                    for (Object item : list) {
                                        inObj.value(item);
                                    }
                                    predicates.add(inObj);
                                }
                            } else {
                                if (fieldClass.isAssignableFrom(String.class)) {
                                    predicates.add(criteriaBuilder.like(root.get(fieldName), "%" + strVal + "%"));
                                } else if (fieldClass.isEnum()) {
                                    if (StringUtils.isNotBlank(strVal)) {
                                        Object enumObj = Enum.valueOf(fieldClass, strVal);
                                        predicates.add(criteriaBuilder.equal(root.get(fieldName), enumObj));
                                    }
                                } else {
                                    predicates.add(criteriaBuilder.equal(root.get(fieldName), fieldValue));
                                }
                            }
                        } catch (Exception e) {
                            log.error("生成实体类查询条件失败:{}:{}:{}", cls.getName(), fieldName, JSON.toJSONString(params), e);
                        }
                    }
                }
            }

            return predicates.size() == 0 ? null : criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        queryCondition.setSpecification(spec);

        return queryCondition;
    }
}
