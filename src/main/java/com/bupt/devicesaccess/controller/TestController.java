package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.mqtt.MqttPushClient;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/")
public class TestController {
    @Autowired
    private MqttPushClient mqttPushClient;

    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value="mainUrl", notes="这只是一个测试controller调用的接口，没有任何的业务逻辑")
    public String mainUrl(){
        return JsonResponseUtil.ok("欢迎使用 devices-access，请访问swagger-ui.html获取接口信息");
    }

    @RequestMapping(value = "/mqtt/publish",method = RequestMethod.GET)
    @ApiOperation(value="mqttPublish", notes="mqtt发送Publish,默认qos为0")
    public String mqttPublish(@RequestParam(value = "messange", defaultValue = "hello mqtt server")  String messange){
        mqttPushClient.publish("hello",messange);
        return JsonResponseUtil.ok("6666");
    }




}
