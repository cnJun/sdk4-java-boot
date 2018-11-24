package com.sdk4.boot.service.impl;

import com.sdk4.boot.Constants;
import com.sdk4.boot.db.PageResult;
import com.sdk4.boot.domain.ApiLog;
import com.sdk4.boot.domain.SmsCode;
import com.sdk4.boot.repository.ApiLogRepository;
import com.sdk4.boot.repository.SmsCodeRepository;
import com.sdk4.boot.service.LogService;
import com.sdk4.boot.util.JpaQueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author sh
 */
@Service("BootLogService")
public class LogServiceImpl implements LogService {
    @Autowired
    ApiLogRepository apiLogRepository;

    @Autowired
    SmsCodeRepository smsCodeRepository;

    @Override
    public PageResult<ApiLog> queryApiLog(Map params) {
        Sort.Order ord1 = new Sort.Order(Sort.Direction.DESC, "reqTime");

        JpaQueryUtils.QueryCondition qc = JpaQueryUtils.createQueryCondition(ApiLog.class, params, ord1);

        Page<ApiLog> page = apiLogRepository.findAll(qc.getSpecification(),
                qc.defaultPageableIfEmpty(0, Constants.DEFAULT_PAGE_SIZE));

        return PageResult.by(page);
    }

    @Override
    public PageResult<SmsCode> querySmsCode(Map params) {
        Sort.Order ord1 = new Sort.Order(Sort.Direction.DESC, "createTime");

        JpaQueryUtils.QueryCondition qc = JpaQueryUtils.createQueryCondition(SmsCode.class, params, ord1);

        Page<SmsCode> page = smsCodeRepository.findAll(qc.getSpecification(),
                qc.defaultPageableIfEmpty(0, Constants.DEFAULT_PAGE_SIZE));

        return PageResult.by(page);
    }
}
