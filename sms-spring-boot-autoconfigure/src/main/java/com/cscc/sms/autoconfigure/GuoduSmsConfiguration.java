package com.cscc.sms.autoconfigure;

import com.cscc.sms.service.SmsService;
import com.cscc.sms.service.guodu.GuoduSmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ajoe.Liu
 * @Date 2019/12/17 11:07
 */
@Configuration(proxyBeanMethods = false)
@Conditional(SmsCondition.class)
@ConditionalOnMissingBean(SmsService.class)
public class GuoduSmsConfiguration {
    @Bean
    GuoduSmsService guoduSmsService(SmsProperties smsProperties) {
        GuoduSmsService smsService = new GuoduSmsService(smsProperties);
        return smsService;
    }
}
