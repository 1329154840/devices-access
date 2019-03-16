package com.bupt.devicesaccess.controller;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
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
 * Date: 2019-03-11
 * Time: 11:43
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */

/**
 * 单个设备的crud操作
 */
@RestController
@Slf4j
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    DeviceRepository deviceRepository;


    /**
     * 查询所有device
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询所有device")
    public String findAll(){
        return JsonResponseUtil.ok(deviceRepository.findAll());
    }
    /**
     * 查询单个deivece
     */
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ApiOperation(value="findById", notes="查询单个deivece")
    public String findById(@RequestParam(value = "id") String id){
        return JsonResponseUtil.ok(deviceRepository.findById(id));
    }
    /**
     * 插入单个Device
     * @param tenantId
     * @param customId
     * @param model
     * @param name
     * @return 返回device
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="insert", notes="插入单个Device")
    public String insert(@RequestParam(value = "tenantId",defaultValue = "-1") Integer tenantId,
                               @RequestParam(value = "customId",defaultValue = "-1") Integer customId,
                               @RequestParam(value = "model") String model,
                               @RequestParam(value = "name") String name){
        Device device = deviceRepository.save(new Device(customId,customId,model,name));
        log.info("insertDevice {}",device);
        return JsonResponseUtil.ok(device);
    }
    /**
     * 按条件更新单个device，传只需要更新的字段，其他字段为null不传
     * @param id
     * @param tenantId
     * @param customId
     * @param model
     * @param name
     * @param nickname
     * @param status
     * @return 返回更新完 device
     */
    @RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="update", notes="按条件更新单个device，传只需要更新的字段，其他字段为null不传")
    public String update(@RequestParam(value = "id") String id,
                               @RequestParam(value = "tenantId",required = false) Integer tenantId,
                               @RequestParam(value = "customId",required = false) Integer customId,
                               @RequestParam(value = "model",required = false) String model,
                               @RequestParam(value = "name",required = false) String name,
                               @RequestParam(value = "nickname",required = false) String nickname,
                               @RequestParam(value = "status",required = false) String status){
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult("device不存在");
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
        log.info("updateDevice {}",newDevice);
        return JsonResponseUtil.ok(newDevice);
    }
    /**
     * 删除单个device
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="deleteById", notes="删除单个device")
    public String deleteById(@RequestParam(value = "id") String id){
        if(!deviceRepository.findById(id).isPresent()){
            return JsonResponseUtil.badResult("device不存在");
        }
        deviceRepository.deleteById(id);
        log.info("deleteDevice {}",id);
        return JsonResponseUtil.ok(id);
    }

}
