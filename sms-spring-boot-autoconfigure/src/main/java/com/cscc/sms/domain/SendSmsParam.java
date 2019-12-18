package com.cscc.sms.domain;

import com.cscc.sms.service.CheckParamNeeded;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ajoe.Liu
 * @Date 2019/12/16 15:01
 */
@Data
@Accessors(chain=true)
public class SendSmsParam implements CheckParamNeeded {
    String bizId;
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
            throw new RuntimeException("PhoneNumbers is empty");
        }
        if(StringUtils.isEmpty(SignName)){
            throw new RuntimeException("SignName is empty");
        }
        if(StringUtils.isEmpty(Content)){
            throw new RuntimeException("Content is empty");
        }
    }
}
