package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.mqtt.MqttPushClient;
import com.bupt.devicesaccess.schedule.RuleSchedule;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;


/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-16
 * Time: 08:06
 * Email:yezuoyao@huli.com
 * 测试
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
@RestController
@RequestMapping("/")
public class TestController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @Autowired
    private RuleSchedule ruleSchedule;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value="mainUrl", notes="这只是一个测试controller调用的接口，没有任何的业务逻辑")
    @HystrixCommand(fallbackMethod="getFallback",commandKey="mainUrl",groupKey="UserGroup",
            threadPoolKey="mainUrlThread")
    public String mainUrl() throws Exception{
//        int i= 1/0;
        TimeUnit.SECONDS.sleep(5);
        return JsonResponseUtil.ok("欢迎使用 devices-access，请访问swagger-ui.html获取接口信息");
    }

    @RequestMapping(value = "/mqtt/publish",method = RequestMethod.GET)
    @ApiOperation(value="mqttPublish", notes="mqtt发送Publish,默认qos为0")
    public String mqttPublish(@RequestParam(value = "messange", defaultValue = "hello mqtt server")  String messange){
        mqttPushClient.publish("/World",messange);
        return JsonResponseUtil.ok("mqtt发送:" + messange);
    }

    private String getFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }
//    @RequestMapping(value = "/getJob",method = RequestMethod.GET)
//    public String getJob(){
//        return ruleSchedule.printJob();
//    }
//
//    @RequestMapping(value = "/date",method = RequestMethod.GET)
//    public String date(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        return JsonResponseUtil.ok(sdf.format(DateBuilder.dateOf(17,12,3)));
//    }

}
