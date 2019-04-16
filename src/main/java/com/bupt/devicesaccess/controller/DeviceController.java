package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.utils.BeanUtil;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
 * Date: 2019-04-11
 * Time: 21:36
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@RestController
@Slf4j
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceRepository deviceRepository;

    public final static int BUFFERLENGHT= 2;

    /**
     * 实时更新设备状态
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/hotUpdate",method = RequestMethod.GET)
    @ApiOperation(value="hotUpdate", notes="实时更新设备状态")
    @HystrixCommand(fallbackMethod="getFallback",commandKey="hotUpdate",groupKey="DeviceGroup",
            threadPoolKey="hotUpdateThread")
    public String hotUpdate(@RequestParam String message) throws Exception{
//        TimeUnit.SECONDS.sleep(5);
        log.info("hotUpdate");
        String[] buffer = message.split("/");
        if (buffer.length == BUFFERLENGHT){
            if (deviceRepository.existsById(buffer[0])){
                deviceRepository.updateById(buffer[0] , buffer[1]);
            }
        }
        return JsonResponseUtil.ok("ok");
    }

    private String getFallback( String message, Throwable e){
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
        }else {
            log.error("{}",e.getMessage());
        }
        return "fail";
    }
}
