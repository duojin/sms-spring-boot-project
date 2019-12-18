package com.cscc.sms.service.guodu;


import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author ajoe.Liu
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
     * 发送短信地址组
     */
    private List<String> sendUrls;

    /**
     * 查询短信地址组
     */
    private List<String> queryUrls;

    /**
     * 短信开关
     */
    private boolean open = false;
}
