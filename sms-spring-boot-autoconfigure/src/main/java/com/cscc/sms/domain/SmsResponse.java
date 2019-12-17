package com.cscc.sms.domain;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 15:26
 */
@Data
public class SmsResponse<D> {
    String BizId;
    String Code;
    String Message;
    String RequestId;
    D data;
}
