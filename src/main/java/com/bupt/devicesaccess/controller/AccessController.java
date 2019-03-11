package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
public class AccessController {
    @Autowired
    DeviceRepository deviceRepository;

    @RequestMapping("/")
    public String mainUrl(){
        return "devices-access";
    }


    @RequestMapping("/insertDevice")
    public String insertDevice(@RequestParam(value = "tenantId",defaultValue = "-1") Integer tenantId,
                               @RequestParam(value = "customId",defaultValue = "-1") Integer customId,
                               @RequestParam(value = "model") String model,
                               @RequestParam(value = "name") String name
                               ){
        Device device = deviceRepository.save(new Device(customId,customId,model,name));
        log.info("insert {}",device);
        return JSON.toJSONString(device);
    }
}
