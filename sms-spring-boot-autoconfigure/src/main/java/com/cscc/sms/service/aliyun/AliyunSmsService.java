package com.cscc.sms.service.aliyun;

import com.cscc.sms.autoconfigure.SmsProperties;
import com.cscc.sms.domain.QuerySendDetailsParam;
import com.cscc.sms.domain.SendBatchSmsParam;
import com.cscc.sms.domain.SendSmsParam;
import com.cscc.sms.domain.SmsResponse;
import com.cscc.sms.service.PageList;
import com.cscc.sms.service.SmsSendDetail;
import com.cscc.sms.service.SmsService;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 17:54
 */
public class AliyunSmsService implements SmsService {

    private SmsProperties smsProperties;

    public AliyunSmsService(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    @Override
    public SmsResponse sendSms(SendSmsParam param) {
        System.out.println("AliyunSmsService sendSms");
        System.out.println("smsProperties="+smsProperties);
        return null;
    }

    @Override
    public SmsResponse sendBatchSms(SendBatchSmsParam param) {
        System.out.println("AliyunSmsService sendBatchSms");
        return null;
    }

    @Override
    public SmsResponse<PageList<SmsSendDetail>> querySendDetails(QuerySendDetailsParam param) {

        System.out.println("AliyunSmsService querySendDetails");
        return null;
    }
}
