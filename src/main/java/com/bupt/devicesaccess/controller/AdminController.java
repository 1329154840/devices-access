package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
import com.bupt.devicesaccess.schedule.RuleSchedule;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-17
 * Time: 09:11
 * Email:yezuoyao@huli.com
 * 管理员操作
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RuleSchedule ruleSchedule;

    /**
     * 查询所有device
     * @return
     */
//    @Token("admin")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询所有device")
    public String findAll(){
        return JsonResponseUtil.ok(deviceRepository.findAll());
    }
    /**
     * 新建设备
     * @param name 必填
     * @param model 必填
     * @param nickname 选填
     * @param groupId 选填
     * @param status 选填
     * @return
     */
//    @Token("admin")
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    @ApiOperation(value="insert", notes="新建设备")
    public String insert(@RequestParam(value = "name") String name,
                         @RequestParam(value = "model") String model,
                         @RequestParam(value = "nickname", required = false) String nickname,
                         @RequestParam(value = "groupId", required = false) String groupId,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "num", defaultValue = "1") Integer num){
        Device device =new Device();
        for (int i=0;i<num;i++){
            device =new Device(model, name);
            if (groupId != null) {
                device.setGroupId(groupId);
            }if (nickname != null){
                device.setNickname(nickname);
            }if (status != null){
                device.setStatus(status);
            }
            deviceRepository.save(device);
        }
        return JsonResponseUtil.ok(device);
    }
    /**
     * 按条件跟新设备，除了id，其他选填
     * @param id
     * @param name
     * @param model
     * @param nickname
     * @param groupId
     * @param status
     * @return
     */
//    @Token("admin")
    @RequestMapping(value = "/updateById", method = RequestMethod.GET)
    @ApiOperation(value="updateById", notes="按条件跟新设备，除了id，其他选填")
    public String updateById(@RequestParam("id") String id,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "model", required = false) String model,
                             @RequestParam(value = "nickname", required = false) String nickname,
                             @RequestParam(value = "groupId", required = false) String groupId,
                             @RequestParam(value = "status", required = false) String status){
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        if (name != null) {
            device.setName(name);
        }if (model != null) {
            device.setModel(model);
        }if (nickname != null) {
            device.setNickname(nickname);
        }if (groupId != null) {
            device.setGroupId(groupId);
        }if (status != null){
            device.setStatus(status);
        }
        Device newDevice = deviceRepository.save(device);
        log.info("updateDevice {}",newDevice);
        return JsonResponseUtil.ok(newDevice);
    }

    /**
     * 通过id删除设备
     * @param id
     * @return
     */
//    @Token("admin")
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    @ApiOperation(value="deleteById", notes="通过id删除设备")
    public String deleteById(@RequestParam("id") String id){
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        deviceRepository.deleteById(id);
        log.info("deleteDevice {}",id);
        return JsonResponseUtil.ok(id);
    }

    /**
     * 一键删除所有设备
     * @return
     */
    @RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
    @ApiOperation(value="deleteAll", notes="删除所有设备")
    public String deleteAll(){
        deviceRepository.deleteAll();
        log.info("deleteDeviceAll");
        return JsonResponseUtil.ok();
    }

    /**
     * 获取所有定时任务
     * @return
     */
    @RequestMapping(value = "/getJob", method = RequestMethod.GET)
    @ApiOperation(value="getJob", notes="获取所有任务")
    public String getJob(){
        return ruleSchedule.printJobByOpenId();
    }
}
