package com.cscc.sms.demo;

import com.cscc.sms.domain.SendSmsParam;
import com.cscc.sms.service.SmsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author ajoe
 * @Date 2019/12/16 18:11
 */
@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class,args);


        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String s : beanNames) {
            System.out.println(s);
        }
        System.out.println("==================================");
        String[] beanNamesForType = applicationContext.getBeanNamesForType(SmsService.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }
        System.out.println("==================================");

//        SmsService bean1 = applicationContext.getBean(SmsService.class);
//        System.out.println(bean1);

    }

    //@Autowired
    //public SmsService smsService;

    @RequestMapping("/")
    public String index(){


        SendSmsParam param = new SendSmsParam();
        //smsService.sendSms(param);



        return "xx";
    }
}
