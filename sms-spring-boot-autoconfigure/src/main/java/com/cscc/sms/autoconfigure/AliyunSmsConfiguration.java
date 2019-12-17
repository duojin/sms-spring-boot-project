package com.cscc.sms.autoconfigure;

import com.cscc.sms.service.aliyun.AliyunSmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/17 11:21
 */
@Configuration(proxyBeanMethods = false)
@Conditional(SmsCondition.class)
@ConditionalOnMissingBean(AliyunSmsService.class)
public class AliyunSmsConfiguration {
    @Bean
    AliyunSmsService aliyunSmsService(SmsProperties smsProperties) {
        AliyunSmsService smsService = new AliyunSmsService(smsProperties);
        return smsService;
    }
}
