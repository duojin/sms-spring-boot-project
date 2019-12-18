package com.cscc.sms.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.StringUtils;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ajoe.Liu
 * @Date 2019/12/17 11:55
 */
public class SmsCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String sourceClass = "";
        if (metadata instanceof ClassMetadata) {
            sourceClass = ((ClassMetadata) metadata).getClassName();
        }
        SmsType smsType = SmsConfigurations.getType(((AnnotationMetadata) metadata).getClassName());

        ConditionMessage.Builder message = ConditionMessage.forCondition("sms", sourceClass);
        Environment environment = context.getEnvironment();
        boolean isMatch = Boolean.valueOf(!StringUtils.isEmpty(environment.getProperty("cscc.sms."+smsType.toString().toLowerCase()+".appid")));
        if (isMatch) {
            return ConditionOutcome.match(message.because("automatic sms type"));
        }
        return ConditionOutcome.noMatch(message.because("unknown sms type"));
    }
}
