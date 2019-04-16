package com.bupt.devicesaccess;
import com.bupt.devicesaccess.mqtt.MqttPushClient;
import io.swagger.annotations.ApiModelProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevicesAccessApplicationTests {
    @Autowired
    private MqttPushClient mqttPushClient;

    @Test
    public void contextLoads(){
        mqttPushClient.subscribe("$queue/topic");
    }
}
