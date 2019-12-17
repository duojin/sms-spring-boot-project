package com.cscc.sms.service;

import com.cscc.sms.domain.QuerySendDetailsParam;
import com.cscc.sms.domain.SendBatchSmsParam;
import com.cscc.sms.domain.SendSmsParam;
import com.cscc.sms.domain.SmsResponse;

/**
 * @Description: 短信服务接口
 * @Author ajoe
 * @Date 2019/12/16 14:35
 */
public interface SmsService {
    /**
     * 发送短信
     * 手机号可多个，发送的短信所有人是一样的
     * @param param
     * @return
     */
    SmsResponse sendSms(SendSmsParam param);

    /**
     * 批量发送短信
     * 手机号可多个，发送的模板为1个，模板注入的参数是可变的，但手机号个数和模板参数组个数必须一样
     * 即一个手机号对应一个模板参数组
     * @param param
     * @return
     */
    SmsResponse sendBatchSms(SendBatchSmsParam param);


    /**
     * 查询发送详情
     * @param param
     * @return
     */
    SmsResponse<PageList<SmsSendDetail>> querySendDetails(QuerySendDetailsParam param);
}
