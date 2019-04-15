package com.bupt.devicesaccess.mqtt;


import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.utils.BeanUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-25
 * Time: 16:50
 * Email:yezuoyao@huli.com
 * 提供 消息接收的回调，以及push消息到达的的回调接口
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
@Component
public class ReceiverCallback implements MqttCallbackExtended {

    public final static String HEARTTOPIC = "/device/status";

    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 3,
            TimeUnit. SECONDS, new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private String url;

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("MQTTReconnect {} serverURI {}", reconnect, serverURI);
    }

    /**
     * 断线回调
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTTConnectionLost");

    }

    /**
     * 消息接收
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String mes = new String(message.getPayload());
        if (HEARTTOPIC.equals(topic)){
            log.info("hotUpdate {}" , mes);
            threadPool.execute(getThread(mes));

        }
    }

    /**
     * 消息送达
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("是否抵达 {}",token.isComplete());
    }

    /**
     * restful线程任务，来热更新
     * @return
     */
    private static Runnable getThread(String mes){
        return new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = BeanUtil.getBean(RestTemplate.class);
                String url = "http://devices-access/device/hotUpdate?message={message}";
                Map<String,String> var= new HashMap<>(1);
                var.put("message",mes);
                restTemplate.getForObject( url, String.class, var);
            }
        };
    }
}
