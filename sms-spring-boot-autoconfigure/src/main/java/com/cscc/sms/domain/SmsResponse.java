package com.cscc.sms.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * @Description: 短信响应定义
 * @author ajoe.Liu
 * @Date 2019/12/16 15:26
 */
@Accessors(chain = true)
@Data
@ToString
public class SmsResponse<D> {
    String code;
    String msg;
    D data;
    String requestId;

    public SmsResponse(){
        this.requestId = UUID.randomUUID().toString().toLowerCase();
    }
}
