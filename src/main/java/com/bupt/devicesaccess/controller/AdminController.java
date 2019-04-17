package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
import com.bupt.devicesaccess.schedule.RuleSchedule;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.bupt.devicesaccess.utils.RequestUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @HystrixCommand(fallbackMethod="findAllGetFallback",commandKey="findAll",groupKey="adminGroup",
            threadPoolKey="findAllThread")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询所有device")
    public String findAll(){
        return JsonResponseUtil.ok(deviceRepository.findAll());
    }

    /**
     * 查找用户所有设备
     * @param openId
     * @return
     */
    @HystrixCommand(fallbackMethod="findByOpenIdGetFallback",commandKey="findByOpenId",groupKey="adminGroup",
            threadPoolKey="findByOpenIdThread")
    @RequestMapping(value = "/findByOpenId", method = RequestMethod.GET)
    @ApiOperation(value="findByOpenId", notes="通过openId查询所有device")
    public String findByCustomId(@RequestParam String openId){
        return JsonResponseUtil.ok( deviceRepository.findByOpenId( openId));
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
    @HystrixCommand(fallbackMethod="insertGetFallback",commandKey="insert",groupKey="adminGroup",
            threadPoolKey="insertThread")
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    @ApiOperation(value="insert", notes="新建设备")
    public String insert(@RequestParam(value = "name") String name,
                         @RequestParam(value = "model") String model,
                         @RequestParam(value = "nickname", required = false) String nickname,
                         @RequestParam(value = "groupId", required = false) String groupId,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "num", defaultValue = "1") Integer num,
                         @RequestParam(value = "openId", required = false,defaultValue = "-1") String openId){
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
            device.setOpenId(openId);

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
    @HystrixCommand(fallbackMethod="updateByIdGetFallback",commandKey="updateById",groupKey="adminGroup",
            threadPoolKey="updateByIdThread")
    @RequestMapping(value = "/updateById", method = RequestMethod.GET)
    @ApiOperation(value="updateById", notes="按条件跟新设备，除了id，其他选填")
    public String updateById(@RequestParam("id") String id,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "model", required = false) String model,
                             @RequestParam(value = "nickname", required = false) String nickname,
                             @RequestParam(value = "groupId", required = false) String groupId,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "openId", required = false) String openId){
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
        }if (openId != null){
            device.setOpenId(openId);
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
    @HystrixCommand(fallbackMethod="deleteByIdGetFallback",commandKey="deleteById",groupKey="adminGroup",
            threadPoolKey="deleteByIdThread")
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
    @HystrixCommand(fallbackMethod="deleteAllGetFallback",commandKey="deleteAll",groupKey="adminGroup",
            threadPoolKey="deleteAllThread")
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
    @HystrixCommand(fallbackMethod="getAllJobGetFallback",commandKey="getAllJob",groupKey="adminGroup",
            threadPoolKey="getAllJobThread")
    @RequestMapping(value = "/getAllJob", method = RequestMethod.GET)
    @ApiOperation(value="getAllJob", notes="获取所有定时任务")
    public String getAllJob(){
        return ruleSchedule.printAllJob();
    }

    /**
     * 删除所有定时任务
     * @return
     */
    @HystrixCommand(fallbackMethod="removeAllJobGetFallback",commandKey="removeAllJob",groupKey="adminGroup",
            threadPoolKey="removeAllJobThread")
    @RequestMapping(value = "/removeAllJob", method = RequestMethod.GET)
    @ApiOperation(value="removeAllJob", notes="删除所有定时任务")
    public String removeAllJob(){
        return ruleSchedule.removeAllJob();
    }

    /**
     * 删除对应用户所有定时任务
     * @return
     */
    @HystrixCommand(fallbackMethod="removeJobByOpenIdGetFallback",commandKey="removeJobByOpenId",groupKey="adminGroup",
            threadPoolKey="removeJobByOpenIdThread")
    @RequestMapping(value = "/removeJobByOpenId", method = RequestMethod.GET)
    @ApiOperation(value="removeJobByOpenId", notes="删除对应用户所有定时任务")
    public String removeJobByOpenId(@RequestParam String openId){
        return ruleSchedule.removeJobByOpenId(openId);
    }

    /**
     * 删除单个定时任务
     * @return
     */
    @HystrixCommand(fallbackMethod="removeJobGetFallback",commandKey="removeJob",groupKey="adminGroup",
            threadPoolKey="removeJobThread")
    @RequestMapping(value = "/removeJob", method = RequestMethod.GET)
    @ApiOperation(value="removeJob", notes="删除单个定时任务")
    public String removeJob(@RequestParam String id,
                                    @RequestParam String op,
                                    @RequestParam String dateStr,
                                    @RequestParam String openId){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
           date = sdf.parse(dateStr);
        } catch (ParseException e) {
            return JsonResponseUtil.badResult( BadResultCode.Date_Error.getCode(), BadResultCode.Date_Error.getRemark());
        }
        return ruleSchedule.removeJob(id , op, date, openId);
    }

    private String findAllGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String findByOpenIdGetFallback(String openId, Throwable e){
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String insertGetFallback(String name,
                                     String model,
                                     String nickname,
                                     String groupId,
                                     String status,
                                     Integer num,
                                     String openId,
                                     Throwable e){
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String updateByIdGetFallback(String id,
                                         String name,
                                         String model,
                                         String nickname,
                                         String groupId,
                                         String status,
                                         String openId,
                                         Throwable e){
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String deleteByIdGetFallback(String id, Throwable e){
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String deleteAllGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String getAllJobGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String removeAllJobGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String removeJobByOpenIdGetFallback(String openId, Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String removeJobGetFallback(String id,
                                        String op,
                                        String dateStr,
                                        String openId,
                                        Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }
}
