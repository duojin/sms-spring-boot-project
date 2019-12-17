package com.cscc.sms.domain;

import com.cscc.sms.service.CheckParamNeeded;
import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 15:01
 */
@Data
public class SendSmsParam implements CheckParamNeeded {
    String[] PhoneNumbers;
    String SignName;
    String TemplateCode;
    String SmsOutNo;
    String SmsUpExtendCode;
    /**
     * 不包括签名
     */
    String Content;

    Map<String,String> TemplateParam;
    Map<String,Object> ExtendParam;

    @Override
    public void checkParam() throws RuntimeException {
        if(ObjectUtils.isEmpty(PhoneNumbers)){
            throw new RuntimeException("PhoneNumbers can not to null");
        }
        if(StringUtils.hasText(SignName)){
            throw new RuntimeException("SignName can not to null");
        }
        if(StringUtils.hasText(Content)){
            throw new RuntimeException("Content can not to null");
        }
    }
}
