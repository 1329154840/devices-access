package com.bupt.devicesaccess.config;

import com.bupt.devicesaccess.mqtt.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-25
 * Time: 16:48
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Component
@Configuration
@Slf4j
public class MqttConfiguration {
    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.username}")
    private String userName;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.topic}")
    private String[] topic;

    @Value("${mqtt.timeout}")
    private int timeout;

    @Value("${mqtt.keepalive}")
    private int keepalive;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getTopic() {
        return topic;
    }

    public void setTopic(String[] topic) {
        this.topic = topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Bean
    public MqttPushClient getMqttPushClient(){
        String newClientId = String.format(clientId + " %s",sdf.format(new Date()));
        MqttPushClient mqttPushClient = new MqttPushClient();
        mqttPushClient.connect(host, newClientId, userName, password, topic, timeout,keepalive);
        log.info("mqttPushClient connect,clientId {}",newClientId);
        return mqttPushClient;
    }

}
