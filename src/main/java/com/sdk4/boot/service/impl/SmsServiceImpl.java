package com.sdk4.boot.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sdk4.boot.CallResult;
import com.sdk4.boot.domain.SmsCode;
import com.sdk4.boot.enums.SmsCodeStatusEnum;
import com.sdk4.boot.repository.SmsCodeRepository;
import com.sdk4.boot.service.SmsService;
import com.sdk4.sms.SmsHelper;
import com.sdk4.sms.SmsResponse;
import com.sdk4.sms.enums.ProviderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 短信接口
 *
 * @author sh
 */
@Slf4j
@Service("BootSmsService")
public class SmsServiceImpl implements SmsService {
    @Autowired
    SmsCodeRepository smsCodeRepository;

    private static final int TIMEOUT = 5 * 60 * 1000;

    @Override
    public CallResult<SmsCode> sendCheckCode(String type, String mobile) {
        CallResult<SmsCode> result = new CallResult<>();

        String code = RandomStringUtils.randomNumeric(4);

        SmsCode smsCode = new SmsCode();
        smsCode.setType(type);
        smsCode.setPhoneArea("+86");
        smsCode.setMobile(mobile);
        smsCode.setCode(code);
        smsCode.setCreateTime(new Date());

        if (StringUtils.isEmpty(type)) {
            result.setError(4, "验证码类型为空");

            smsCode.setStatus(SmsCodeStatusEnum.FAIL.getCode());
            smsCode.setStatusDesc(result.getMessage());
        } else if (!SmsHelper.containTemplate(type)) {
            result.setError(4, "验证码类型不存在[" + type + "]");

            smsCode.setStatus(SmsCodeStatusEnum.FAIL.getCode());
            smsCode.setStatusDesc(result.getMessage());
        } else {
            Map<String, String> params = Maps.newHashMap();
            params.put("code", code);

            SmsResponse ret = SmsHelper.send(type, mobile, params);
            if (ret.success()) {
                smsCode.setStatus(SmsCodeStatusEnum.PENDING.getCode());
                result.setCode(0);
            } else {
                smsCode.setStatus(SmsCodeStatusEnum.FAIL.getCode());
                result.setCode(4);

                if (ret.getException() != null) {
                    log.error("发送短信发送异常:{}:{}:{}", type, mobile, code, ret.getException());
                }
            }

            smsCode.setMsgId(ret.getMsgId());
            smsCode.setStatusDesc(ret.getMessage());

            result.setMessage(ret.getMessage());

            SmsHelper.Cfg cfg = ret.getCfg();
            if (cfg != null) {
                if (cfg.getProvider().getProvider() == ProviderEnum.test) {
                    smsCode.setCode("1234");
                }
                smsCode.setChannelName(cfg.getProvider().getProvider().getText());
            }
        }

        result.setData(smsCode);

        try {
            this.smsCodeRepository.save(smsCode);
        } catch (Exception e) {
            log.error("发送短信验证结果写库失败:{}", JSON.toJSONString(smsCode), e);
        }

        return result;
    }

    @Override
    public CallResult<SmsCode> verifyCheckCode(String type, String mobile, String code) {
        CallResult<SmsCode> result = new CallResult<>();

        SmsCode where = new SmsCode();
        where.setMobile(mobile);
        where.setStatus(SmsCodeStatusEnum.PENDING.getCode());

        List<SmsCode> list = this.smsCodeRepository.findAll(Example.of(where), new Sort(Sort.Direction.DESC, "createTime"));

        boolean check = false;
        List<SmsCode> listWaitingSave = Lists.newArrayList();
        SmsCode checkSuccess = null;

        for (SmsCode row : list) {
            if (check) {
                row.setUsedTime(new Date());
                row.setStatus(SmsCodeStatusEnum.FAIL.getCode());
                row.setStatusDesc("已重新下发,作废");

                listWaitingSave.add(row);
            } else if (System.currentTimeMillis() - row.getCreateTime().getTime() > TIMEOUT) {
                row.setUsedTime(new Date());
                row.setStatus(SmsCodeStatusEnum.FAIL.getCode());
                row.setStatusDesc("超时" + TIMEOUT + "ms");

                listWaitingSave.add(row);
            } else {
                if (StringUtils.equalsIgnoreCase(code, row.getCode())) {
                    check = true;
                    checkSuccess = row;

                    row.setUsedTime(new Date());
                    row.setStatus(SmsCodeStatusEnum.SUCCESS.getCode());
                    row.setStatusDesc("验证成功:" + code);

                    listWaitingSave.add(row);
                }
            }
        }

        if (!listWaitingSave.isEmpty()) {
            try {
                this.smsCodeRepository.saveAll(listWaitingSave);
            } catch (Exception e) {
                log.error("短信验证结果写库异常:{}", JSON.toJSONString(listWaitingSave), e);
            }
        }

        if (check) {
            result.setCode(0);
            result.setMessage("验证成功");
            result.setData(checkSuccess);
        } else {
            result.setCode(4);
            result.setMessage("验证失败");
        }

        return result;
    }
}
