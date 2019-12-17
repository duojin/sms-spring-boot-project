package com.cscc.sms.service.guodu;

import com.cscc.sms.autoconfigure.SmsProperties;
import com.cscc.sms.domain.QuerySendDetailsParam;
import com.cscc.sms.domain.SendBatchSmsParam;
import com.cscc.sms.domain.SendSmsParam;
import com.cscc.sms.domain.SmsResponse;
import com.cscc.sms.service.PageList;
import com.cscc.sms.service.SmsSendDetail;
import com.cscc.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 17:54
 */
@Slf4j
public class GuoduSmsService implements SmsService {

    private SmsProperties smsProperties;

    private String appid;
    private String appkey;
    private List<String> serverBaseUrls;

    public GuoduSmsService(SmsProperties smsProperties) {
        log.debug("{}正在构造,smsProperties={}",this.getClass().getName(),smsProperties);

        this.smsProperties = smsProperties;
        this.appid = smsProperties.getGuodu().getAppid();
        this.appkey = smsProperties.getGuodu().getAppkey();
        this.serverBaseUrls = smsProperties.getGuodu().getServerBaseUrls();

        Assert.hasText(appid,"appid is empty or null");
        Assert.hasText(appkey,"appkey is empty or null");
        Assert.notEmpty(serverBaseUrls,"serverBaseUrls is empty");
    }

    @Override
    public SmsResponse sendSms(SendSmsParam param) {
        log.debug("{} 准备发送短信,param={}",this.getClass().getName(),param);

        //simple check
        param.checkParam();
        Map requestParam = buildQureyParam(param);




        return null;
    }

    private Map buildQureyParam(SendSmsParam param) {
        Map<String, String> queryParas = new HashMap<>();
        queryParas.put("OperID", this.appid);
        queryParas.put("OperPass", this.appkey);
        queryParas.put("DesMobile", StringUtils.arrayToCommaDelimitedString(param.getPhoneNumbers()));
        queryParas.put("Content", warpSignName(param.getSignName()) + param.getContent());
        queryParas.put("Content_Code", "1");

        Map extendMap = param.getExtendParam();
        if(!ObjectUtils.isEmpty(extendMap)){
            String SendTime = String.valueOf(extendMap.get("SendTime"));
            try{
                LocalDateTime.parse(SendTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            }catch (Exception e){
                queryParas.put("SendTime", SendTime);
            }
            String ValidTime = String.valueOf(extendMap.get("ValidTime"));
            try{
                LocalDateTime.parse(ValidTime, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            }catch (Exception e){
                queryParas.put("ValidTime", ValidTime);
            }
        }
        if(StringUtils.hasText(param.getSmsUpExtendCode())){
            queryParas.put("AppendID", param.getSmsUpExtendCode());
        }
        return queryParas;
    }

    private String warpSignName(String signName) {
        return "【"+signName+"】";
    }

    @Override
    public SmsResponse sendBatchSms(SendBatchSmsParam param) {
        System.out.println("GuoduSmsService sendBatchSms");
        return null;
    }

    @Override
    public SmsResponse<PageList<SmsSendDetail>> querySendDetails(QuerySendDetailsParam param) {

        System.out.println("GuoduSmsService querySendDetails");
        return null;
    }
}
