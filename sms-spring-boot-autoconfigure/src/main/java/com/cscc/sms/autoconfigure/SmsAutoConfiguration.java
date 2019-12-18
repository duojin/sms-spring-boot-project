package com.cscc.sms.autoconfigure;

import com.cscc.sms.service.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description: 自动配置入口
 * @author ajoe.Liu
 * @Date 2019/12/16 17:34
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SmsService.class)
@EnableConfigurationProperties({SmsProperties.class})
@Import({SmsAutoConfiguration.SmsConfigurationImportSelector.class})
public class SmsAutoConfiguration {
    static class SmsConfigurationImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            SmsType[] types = SmsType.values();
            String[] imports = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                imports[i] = SmsConfigurations.getConfigurationClass(types[i]);
            }
            return imports;
        }

    }
}
