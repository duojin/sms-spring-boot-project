package com.cscc.sms.demo;

import com.cscc.sms.domain.SendSmsParam;
import com.cscc.sms.service.SmsService;
import com.cscc.sms.service.guodu.GuoduSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ajoe.Liu
 * @Date 2019/12/16 18:11
 */
@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class,args);
    }

    @Autowired
    public GuoduSmsService guoduSmsService;

    @RequestMapping("/")
    public String index(){
//        SendSmsParam param = new SendSmsParam();
//        param.setContent("xxxx");
//        param.setPhoneNumbers(new String[]{"1360000xxxx"});
//        param.setSignName("智慧");
//        guoduSmsService.sendSms(param);
        return guoduSmsService.queryBalance().toString();
    }
}
