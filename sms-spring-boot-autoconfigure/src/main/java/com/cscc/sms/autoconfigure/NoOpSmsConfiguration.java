package com.cscc.sms.autoconfigure;

import com.cscc.sms.service.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ajoe.Liu
 * @Date 2019/12/17 11:21
 */
@ConditionalOnMissingBean(SmsService.class)
public class NoOpSmsConfiguration {
}
