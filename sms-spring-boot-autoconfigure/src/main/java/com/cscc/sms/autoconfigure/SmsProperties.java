package com.cscc.sms.autoconfigure;

import com.cscc.sms.service.aliyun.AliyunSmsProperties;
import com.cscc.sms.service.guodu.GuoduSmsProperties;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 短信配置类
 * @author ajoe.Liu
 * @Date 2019/12/16 17:35
 */
@Data
@ToString
@ConfigurationProperties(prefix = "cscc.sms")
public class SmsProperties {
    private final AliyunSmsProperties aliyun = new AliyunSmsProperties();
    private final GuoduSmsProperties guodu = new GuoduSmsProperties();
}
