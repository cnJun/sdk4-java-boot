package com.sdk4.boot.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sdk4.boot.domain.SysConfig;
import com.sdk4.boot.repository.SysConfigRepository;
import com.sdk4.boot.service.SysConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

/**
 * @author sh
 */
@Service("BootSysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
    @Autowired
    SysConfigRepository sysConfigRepository;

    @Override
    public SysConfig save(String name, String value) {
        SysConfig result = null;

        SysConfig where = new SysConfig();
        where.setName(name);

        List<SysConfig> list = sysConfigRepository.findAll(Example.of(where));
        if (list.isEmpty()) {
            result = new SysConfig();
            result.setName(name);
        } else {
            result = list.get(0);
        }
        result.setValue(value);

        try {
            sysConfigRepository.save(result);
        } catch (Exception e) {
            result = null;
        }

        return result;
    }

    @Override
    public SysConfig get(String name) {
        SysConfig where = new SysConfig();
        where.setName(name);

        List<SysConfig> list = sysConfigRepository.findAll(Example.of(where));

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Map<String, SysConfig> get(List<String> nameList) {
        Map<String, SysConfig> result = Maps.newHashMap();

        Specification<SysConfig> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = Lists.newArrayList();

            if (CollectionUtils.isNotEmpty(nameList)) {
                CriteriaBuilder.In in = criteriaBuilder.in(root.get("name"));
                for (String name : nameList) {
                    in.value(name);
                }
                predicates.add(criteriaBuilder.and(in));
            }

            return predicates.isEmpty() ? null : criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        List<SysConfig> list = sysConfigRepository.findAll(spec);
        for (SysConfig item : list) {
            result.put(item.getName(), item);
        }

        return result;
    }
}
