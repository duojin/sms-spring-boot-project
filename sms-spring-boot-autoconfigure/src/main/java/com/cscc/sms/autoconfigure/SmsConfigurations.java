package com.cscc.sms.autoconfigure;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @Description: 短信配置池
 * @Author ajoe
 * @Date 2019/12/17 11:19
 */
public class SmsConfigurations {
    private static final Map<SmsType, Class<?>> MAPPINGS;
    static {
        Map<SmsType, Class<?>> mappings = new EnumMap<>(SmsType.class);
        mappings.put(SmsType.GUODU, GuoduSmsConfiguration.class);
        mappings.put(SmsType.ALIYUN, AliyunSmsConfiguration.class);
        mappings.put(SmsType.NONE, NoOpSmsConfiguration.class);
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }
    private SmsConfigurations() {
    }
    static String getConfigurationClass(SmsType smsType) {
        Class<?> configurationClass = MAPPINGS.get(smsType);
        Assert.state(configurationClass != null, () -> "Unknown sms type " + smsType);
        return configurationClass.getName();
    }
    static SmsType getType(String configurationClassName) {
        for (Map.Entry<SmsType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(configurationClassName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Unknown configuration class " + configurationClassName);
    }
}
