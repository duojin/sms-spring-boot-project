package com.cscc.sms.service.guodu;

import com.cscc.kit.util.MiniHttpClientUtils;
import com.cscc.sms.autoconfigure.SmsProperties;
import com.cscc.sms.domain.*;
import com.cscc.sms.service.PageList;
import com.cscc.sms.service.SmsSendDetail;
import com.cscc.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 国都企信通短信服务
 * @author ajoe.Liu
 * @Date 2019/12/16 17:54
 */
@Slf4j
public class GuoduSmsService implements SmsService {

    private SmsProperties smsProperties;

    private String appid;
    private String appkey;
    private boolean open;
    private List<String> sendUrls;
    private List<String> queryUrls;

    public GuoduSmsService(SmsProperties smsProperties) {
        log.debug("{}正在构造,smsProperties={}",this.getClass().getName(),smsProperties);

        this.smsProperties = smsProperties;
        this.appid = smsProperties.getGuodu().getAppid();
        this.appkey = smsProperties.getGuodu().getAppkey();
        this.open = smsProperties.getGuodu().isOpen();
        this.sendUrls = smsProperties.getGuodu().getSendUrls();
        this.queryUrls = smsProperties.getGuodu().getQueryUrls();

        Assert.hasText(appid,"appid is empty or null");
        Assert.hasText(appkey,"appkey is empty or null");
        Assert.notEmpty(sendUrls,"sendUrls is empty");
        Assert.notEmpty(queryUrls,"queryUrls is empty");
    }

    @Override
    public SmsResponse sendSms(SendSmsParam param) {
        log.debug("{} 准备发送短信,param={}",this.getClass().getName(),param);

        //simple check
        param.checkParam();
        Map<String, String> queryParas = buildQureyParam(param);

        try{
            String responseXml = loopSend(param, queryParas);

            return new SmsResponse()
                    .setCode(ResultCode.SUCCESS)
                    .setMsg("发送成功")
                    .setData(responseXml);
        }catch (Exception e){
            return new SmsResponse()
                    .setCode(ResultCode.FAIL)
                    .setMsg(e.getMessage());
        }

    }

    private String loopSend(SendSmsParam param, Map<String, String> queryParas) {
        if(!open){
            return "open=false";
        }
        String responseString = "";
        int urlSize = sendUrls.size();
        List<String> tempUrls = new ArrayList<>(urlSize);
        sendUrls.forEach(url->tempUrls.add(url));
        int tryCount = urlSize;
        boolean isUrlChanged = false;
        while (true){
            String trySendUrl = tempUrls.get(0);
            try {
                log.info("发送短信使用地址为===>{}",trySendUrl);
                responseString = MiniHttpClientUtils.get(trySendUrl,queryParas);
                log.debug("{} 请求发送短信同步完成,url={},requestParam={}",this.getClass().getName(),trySendUrl,param);
                log.debug("{} 请求发送短信同步完成,response={}",this.getClass().getName(),responseString);
                break;
            }catch (Exception e){
                log.warn("发送短信[Exception]:{}",e.getMessage());
                tryCount--;
                if(tryCount>0){
                    tempUrls.remove(0);
                    tempUrls.add(trySendUrl);
                    isUrlChanged = true;
                }
            }
            if(tryCount==0){
                log.error("请求地址{}试验完毕", StringUtils.arrayToCommaDelimitedString(sendUrls.toArray()));
                throw new RuntimeException("所有配置短信地址均请求失败");
            }
        }

        if(isUrlChanged){
            synchronized (sendUrls){
                sendUrls.clear();
                tempUrls.forEach(url-> sendUrls.add(url));
            }
        }

        return responseString;
    }

    private Map<String, String> buildQureyParam(SendSmsParam param) {
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


    public SmsResponse queryBalance() {
        try{
            String tryQueryUrl = queryUrls.get(0);
            Map<String, String> queryParas = new HashMap<>();
            queryParas.put("OperID", this.appid);
            queryParas.put("OperPass", this.appkey);
            String responseString = MiniHttpClientUtils.get(tryQueryUrl,queryParas);
            return new SmsResponse()
                    .setCode(ResultCode.SUCCESS)
                    .setMsg("查询成功")
                    .setData(responseString);
        }catch (Exception e){
            return new SmsResponse()
                    .setCode(ResultCode.FAIL)
                    .setMsg(e.getMessage());
        }
    }
}
