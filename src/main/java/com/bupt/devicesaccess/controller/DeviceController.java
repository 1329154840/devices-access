package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 11:43
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@RestController
@Slf4j
public class DeviceController {
    @Autowired
    DeviceRepository deviceRepository;

    @RequestMapping("/")
    public String mainUrl(){
        return "devices-access";
    }

    @RequestMapping("/findAllDevice")
    public String findAllDevice(){
        return JSON.toJSONString(deviceRepository.findAll());
    }

    @RequestMapping("/findDeviceById")
    public String findDeviceById(@RequestParam(value = "id") String id){
        return JSON.toJSONString(deviceRepository.findById(id));
    }

    @RequestMapping("/insertDevice")
    public String insertDevice(@RequestParam(value = "tenantId",defaultValue = "-1") Integer tenantId,
                               @RequestParam(value = "customId",defaultValue = "-1") Integer customId,
                               @RequestParam(value = "model") String model,
                               @RequestParam(value = "name") String name){
        Device device = deviceRepository.save(new Device(customId,customId,model,name));
        log.info("insert {}",device);
        return JSON.toJSONString(device);
    }

    @RequestMapping("/updateDevice")
    public String updateDevice(@RequestParam(value = "id") String id,
                               @RequestParam(value = "tenantId",required = false) Integer tenantId,
                               @RequestParam(value = "customId",required = false) Integer customId,
                               @RequestParam(value = "model",required = false) String model,
                               @RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "nickname",required = false) String nickname,
                               @RequestParam(value = "status",required = false) String status){
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JSON.toJSONString(null);
        }
        Device device = optionalDevice.get();
        if (tenantId != null){
            device.setTenantId(tenantId);
        }if (customId != null){
            device.setCustomId(customId);
        }if (model != null){
            device.setModel(model);
        }if (name != null){
            device.setName(name);
        }if (nickname != null){
            device.setNickname(nickname);
        }if (status != null){
            device.setStatus(status);
        }
        Device newDevice = deviceRepository.save(device);
        log.info("update {}",newDevice);
        return JSON.toJSONString(newDevice);
    }

    @RequestMapping("/deleteDeviceById")
    public String DeleteById(@RequestParam(value = "id") String id){
        if(!deviceRepository.findById(id).isPresent()){
            return JSON.toJSONString(null);
        }
        deviceRepository.deleteById(id);
        log.info("delete {}",id);
        return JSON.toJSONString(id);
    }

}