package com.bupt.devicesaccess.mqtt;


import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.utils.BeanUtil;
import jnr.ffi.annotations.In;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

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
public class ReceiverCallback implements MqttCallback {
    private DeviceRepository deviceRepository;

    public final static String HEARTTOPIC = "/device/status";

    public final static int BUFFERLENGHT= 2;

    /**
     * 断线回调
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("mqtt连接断开，可以做重连");
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
            hotUpdate(mes);
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
     * 实时更新状态
     * @param message
     */
    private void hotUpdate(String message){
        String[] buffer = message.split("/");
        if (buffer.length == BUFFERLENGHT){
            try{
                getRepositoryBean();
                if (deviceRepository.existsById(buffer[0])){
                    deviceRepository.updateById(buffer[0] , buffer[1]);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Autowired自动注入失效，采用手动注入 deviceRepository
     * @throws Exception
     */
    private void getRepositoryBean() throws Exception{
        if (deviceRepository == null){
            deviceRepository = BeanUtil.getBean(DeviceRepository.class);
            if (deviceRepository == null){
                throw new Exception("deviceRepository 获取bean为空");
            }
        }
    }

}
