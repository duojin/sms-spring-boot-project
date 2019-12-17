package com.cscc.sms.domain;

import com.cscc.sms.service.CheckParamNeeded;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 16:00
 */
public class SendBatchSmsParam implements CheckParamNeeded {
    String[] PhoneNumbers;
    String SignName;
    String TemplateCode;
    String SmsOutNo;
    String SmsUpExtendCode;

    List<Map<String,String>> TemplateParams;

    @Override
    public void checkParam() throws RuntimeException {
        if(ObjectUtils.isEmpty(PhoneNumbers)){
            throw new RuntimeException("PhoneNumbers can not to null");
        }
        if(StringUtils.hasText(SignName)){
            throw new RuntimeException("SignName can not to null");
        }
    }
}