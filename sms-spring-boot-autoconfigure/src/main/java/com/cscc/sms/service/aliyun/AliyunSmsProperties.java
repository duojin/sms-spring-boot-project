package com.cscc.sms.service.aliyun;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author ajoe
 */
@Data
@ToString
@EnableConfigurationProperties(AliyunSmsProperties.class)
@ConfigurationProperties(prefix = AliyunSmsProperties.PREFIX)
public class AliyunSmsProperties {

    public static final String PREFIX = "cscc.sms.aliyun";
    private String appid;
    private String aliyunkey;
}
