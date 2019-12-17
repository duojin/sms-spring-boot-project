package com.cscc.sms.service.guodu;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author ajoe
 */
@Data
@ToString
@EnableConfigurationProperties(GuoduSmsProperties.class)
@ConfigurationProperties(prefix = GuoduSmsProperties.PREFIX)
public class GuoduSmsProperties {

    public static final String PREFIX = "cscc.sms.guodu";
    /**
     * OperID
     */
    private String appid;
    /**
     * OperPass
     */
    private String appkey;

    /**
     * 请求基地址
     */
    private List<String> serverBaseUrls;
}
