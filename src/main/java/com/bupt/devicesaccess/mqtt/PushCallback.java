package com.bupt.devicesaccess.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-25
 * Time: 16:50
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
public class PushCallback implements MqttCallback {
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("mqtt连接断开，可以做重连");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息主题 {}", topic);
        log.info("接收消息Qos {}", message.getQos());
        log.info("接收消息内容 {}", new String(message.getPayload()) );
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("是否抵达 {} {}",token.isComplete());
    }
}
