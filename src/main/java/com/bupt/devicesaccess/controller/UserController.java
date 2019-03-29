package com.bupt.devicesaccess.controller;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.model.Device;
import com.bupt.devicesaccess.schedule.RuleSchedule;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.bupt.devicesaccess.utils.RequestUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * Date: 2019-03-11
 * Time: 11:43
 * Email:yezuoyao@huli.com
 * 用户操作
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private RuleSchedule ruleSchedule;

    /**
     * 查询未绑定的所有空设备
     * @return
     */
//    @Token
    @RequestMapping(value = "/findFreeAll", method = RequestMethod.GET)
    @ApiOperation(value="findFreeAll", notes="查询自己拥有的所有device")
    public String findFreeAll(){
        Integer uid = RequestUtils.getOpenId();
        return JsonResponseUtil.ok(deviceRepository.findFreeAll(uid));
    }
    /**
     * 查询自己拥有的所有设备
     * @return
     */
//    @Token
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询自己拥有的所有device")
    public String findAll(){
        Integer uid = RequestUtils.getOpenId();
        return JsonResponseUtil.ok(deviceRepository.findByCustomId(uid));
    }
    /**
     * 查询自己拥有的组号
     * @return
     */
//    @Token
    @RequestMapping(value = "/findGroupId", method = RequestMethod.GET)
    @ApiOperation(value="findGroupId", notes="查询自己拥有的组号")
    public String findGroupId(){
        Integer openId = RequestUtils.getOpenId();
        return JsonResponseUtil.ok(deviceRepository.findGroupId(openId));
    }
    /**
     * 查询自己拥有对应组的device
     * @return
     */
//    @Token
    @RequestMapping(value = "/findByGroupId", method = RequestMethod.GET)
    @ApiOperation(value="findByGroupId", notes="查询自己拥有对应组的device")
    public String findByGroupId(@RequestParam(value = "groupId") String groupId){
        Integer uid = RequestUtils.getOpenId();
        return JsonResponseUtil.ok(deviceRepository.findByCustomIdAndGroupId(uid, groupId));
    }
    /**
     * 查询单个device
     */
//    @Token
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ApiOperation(value="findById", notes="查询单个device")
    public String findById(@RequestParam(value = "id") String id){
        return JsonResponseUtil.ok(deviceRepository.findById(id));
    }
    /**
     * 将新建空设备，拉入自己组下
     * @return 返回device
     */
//    @Token
    @RequestMapping(value = "/insert", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="insert", notes="将新建空设备，拉入自己组下")
    public String insert(@RequestParam(value = "id") String id,
                         @RequestParam(value = "group_id") String groupId){
        Integer uid = RequestUtils.getOpenId();
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        device.setCustomId(uid);
        device.setGroupId(groupId);
        device.setStatus("关机");
        Device newDevice = deviceRepository.save(device);
        log.info("uid:{} insertDevice {}",uid,newDevice);
        return JsonResponseUtil.ok(newDevice);
    }
    /**
     * 按条件更新单个device，传只需要更新的字段，其他字段为null不传
     * @param id
     * @param groupId
     * @param nickname
     * @param status
     * @return 返回更新完 device
     */
//    @Token
    @RequestMapping(value = "/update", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="update", notes="按条件更新单个device，传只需要更新的字段，其他字段为null不传")
    public String update(@RequestParam(value = "id") String id,
                               @RequestParam(value = "groupId",required = false) String groupId,
                               @RequestParam(value = "nickname",required = false) String nickname,
                               @RequestParam(value = "status",required = false) String status){
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        if (groupId != null) {
            device.setGroupId(groupId);
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
     * 解绑该设备
     * @param id
     * @return
     */
//    @Token
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="deleteById", notes="解绑该设备")
    public String deleteById(@RequestParam(value = "id") String id){
        Integer uid = RequestUtils.getOpenId();
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        device.setStatus("关机");
        device.setCustomId(-1);
        device.setGroupId("-1");
        device.setNickname("");
        Device newDevice = deviceRepository.save(device);
        log.info("uid:{} deleteDevice:{}",uid,id);
        return JsonResponseUtil.ok(newDevice);
    }

    /**
     * 上传规则,启用定时任务
     * @param rule
     * @return
     */
    @RequestMapping(value = "/upload",method = RequestMethod.GET)
    @ApiOperation(value="upload", notes="上传规则")
    public String upload(@RequestParam String rule){
        String result;
        try {
            JSONObject jsonRule =JSONObject.parseObject(rule);
            result =ruleSchedule.adapter(jsonRule);
        } catch (JSONException e){
            log.error("json 解析有误");
            return JsonResponseUtil.badResult( BadResultCode.Rule_Json_Error.getCode(), BadResultCode.Rule_Json_Error.getRemark());
        }
        return result;
    }

    /**
     * 获取个人定时任务
     * @return
     */
    @RequestMapping(value = "/getJob", method = RequestMethod.GET)
    @ApiOperation(value="getJob", notes="获取个人定时任务")
    public String getJob(){
        return ruleSchedule.printJobByOpenId();
    }

    /**
     * 删除该用户所有定时任务
     * @return
     */
    @RequestMapping(value = "/removeJobByOpenId", method = RequestMethod.GET)
    @ApiOperation(value="removeJobByOpenId", notes="获取个人定时任务")
    public String removeJobByOpenId(){
        String openId = String.valueOf( RequestUtils.getOpenId() );
        return ruleSchedule.removeJobByOpenId(openId);
    }

    /**
     * 删除单个定时任务
     * @return
     */
    @RequestMapping(value = "/removeJob", method = RequestMethod.GET)
    @ApiOperation(value="removeJob", notes="删除单个定时任务")
    public String removeJob(@RequestParam String id,
                                    @RequestParam String op,
                                    @RequestParam String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String openId = String.valueOf(RequestUtils.getOpenId());
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            return JsonResponseUtil.badResult( BadResultCode.Date_Error.getCode(), BadResultCode.Date_Error.getRemark());
        }
        return ruleSchedule.removeJob(id , op, date, openId);
    }
}
