package com.bupt.devicesaccess;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.mqtt.MqttPushClient;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.DateBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DevicesAccessApplicationTests {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void contextLoads(){
        JSONArray rule = new JSONArray();
        JSONObject singleRule = new JSONObject();
        singleRule.put("id","a531581b-1d69-4c46-9607-00febc2c760b");
        singleRule.put("op","OFF");
        singleRule.put("date", DateBuilder.dateOf(15,59,0).getTime());
        singleRule.put("type","1");
        rule.add(singleRule);
        JSONObject result = new JSONObject();
        result.put("rule",rule);
        String response = restTemplate.getForObject("http://devices-access/user/upload?rule={rule}",String.class,JSONObject.toJSONString(result));
        log.info("{}",response);
    }
}
