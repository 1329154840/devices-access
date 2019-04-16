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
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


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
    @HystrixCommand(fallbackMethod="findFreeAllGetFallback",commandKey="findFreeAll",groupKey="UserGroup",
            threadPoolKey="findFreeAllThread")
    @RequestMapping(value = "/findFreeAll", method = RequestMethod.GET)
    @ApiOperation(value="findFreeAll", notes="查询自己拥有的所有device")
    public String findFreeAll() throws Exception{
        return JsonResponseUtil.ok(deviceRepository.findFreeAll( RequestUtils.getOpenId()));
    }
    /**
     * 查询自己拥有的所有设备
     * @return
     */
//    @Token
    @HystrixCommand(fallbackMethod="findAllGetFallback",commandKey="findAll",groupKey="UserGroup",
            threadPoolKey="findAllThread")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询自己拥有的所有device")
    public String findAll(){
        return JsonResponseUtil.ok(deviceRepository.findByOpenId( RequestUtils.getOpenId()));
    }
    /**
     * 查询自己拥有的组号
     * @return
     */
//    @Token
    @HystrixCommand(fallbackMethod="findGroupIdGetFallback",commandKey="findGroupId",groupKey="UserGroup",
            threadPoolKey="findGroupIdThread")
    @RequestMapping(value = "/findGroupId", method = RequestMethod.GET)
    @ApiOperation(value="findGroupId", notes="查询自己拥有的组号")
    public String findGroupId(){
        return JsonResponseUtil.ok(deviceRepository.findGroupId( RequestUtils.getOpenId()));
    }
    /**
     * 查询自己拥有对应组的device
     * @return
     */
//    @Token
    @HystrixCommand(fallbackMethod="findByGroupIdGetFallback",commandKey="findByGroupId",groupKey="UserGroup",
            threadPoolKey="findByGroupIdThread")
    @RequestMapping(value = "/findByGroupId", method = RequestMethod.GET)
    @ApiOperation(value="findByGroupId", notes="查询自己拥有对应组的device")
    public String findByGroupId(@RequestParam(value = "groupId") String groupId){
        return JsonResponseUtil.ok(deviceRepository.findByOpenIdAndGroupId( RequestUtils.getOpenId(), groupId));
    }
    /**
     * 查询单个device
     */
//    @Token
    @HystrixCommand(fallbackMethod="findByIdGetFallback",commandKey="findById",groupKey="UserGroup",
            threadPoolKey="findByIdThread")
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
    @HystrixCommand(fallbackMethod="insertGetFallback",commandKey="insert",groupKey="UserGroup",
            threadPoolKey="insertThread")
    @RequestMapping(value = "/insert", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="insert", notes="将新建空设备，拉入自己组下")
    public String insert(@RequestParam(value = "id") String id,
                         @RequestParam(value = "group_id") String groupId){
        String openId = RequestUtils.getOpenId();
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        device.setOpenId(openId);
        device.setGroupId(groupId);
        device.setStatus("关机");
        Device newDevice = deviceRepository.save(device);
        log.info("uid:{} insertDevice {}",openId,newDevice);
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
    @HystrixCommand(fallbackMethod="updateGetFallback",commandKey="update",groupKey="UserGroup",
            threadPoolKey="updateThread")
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
    @HystrixCommand(fallbackMethod="deleteByIdGetFallback",commandKey="deleteById",groupKey="UserGroup",
            threadPoolKey="deleteByIdThread")
    @RequestMapping(value = "/deleteById", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="deleteById", notes="解绑该设备")
    public String deleteById(@RequestParam(value = "id") String id){
        String openId = RequestUtils.getOpenId();
        Optional<Device> optionalDevice =deviceRepository.findById(id);
        if(!optionalDevice.isPresent()){
            return JsonResponseUtil.badResult(BadResultCode.Device_Is_Null.getCode(),BadResultCode.Device_Is_Null.getRemark());
        }
        Device device = optionalDevice.get();
        device.setStatus("关机");
        device.setOpenId("-1");
        device.setGroupId("-1");
        device.setNickname("");
        Device newDevice = deviceRepository.save(device);
        log.info("uid:{} deleteDevice:{}", openId, id);
        return JsonResponseUtil.ok(newDevice);
    }

    /**
     * 上传规则,启用定时任务
     * @param rule
     * @return
     */
    @HystrixCommand(fallbackMethod="uploadGetFallback",commandKey="upload",groupKey="UserGroup",
            threadPoolKey="uploadThread")
    @RequestMapping(value = "/upload",method = { RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value="upload", notes="上传规则")
    public String upload(@RequestParam(value = "rule",required = false) String rule,HttpServletRequest request){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        log.info("rule={}",sb);
        String result;
        try {
            JSONObject jsonRule =JSONObject.parseObject(sb.toString());
            log.info("",jsonRule);
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
    @HystrixCommand(fallbackMethod="getJobGetFallback",commandKey="getJob",groupKey="UserGroup",
            threadPoolKey="getJobThread")
    @RequestMapping(value = "/getJob", method = RequestMethod.GET)
    @ApiOperation(value="getJob", notes="获取个人定时任务")
    public String getJob(){
        return ruleSchedule.printJobByOpenId();
    }

    /**
     * 删除该用户所有定时任务
     * @return
     */
    @HystrixCommand(fallbackMethod="removeJobByOpenIdGetFallback",commandKey="removeJobByOpenId",groupKey="UserGroup",
            threadPoolKey="removeJobByOpenIdThread")
    @RequestMapping(value = "/removeJobByOpenId", method = RequestMethod.GET)
    @ApiOperation(value="removeJobByOpenId", notes="删除该用户所有定时任务")
    public String removeJobByOpenId(){
        return ruleSchedule.removeJobByOpenId(RequestUtils.getOpenId());
    }

    /**
     * 删除单个定时任务
     * @return
     */
    @HystrixCommand(fallbackMethod="removeJobGetFallback",commandKey="removeJob",groupKey="UserGroup",
            threadPoolKey="removeJobThread")
    @RequestMapping(value = "/removeJob", method = RequestMethod.GET)
    @ApiOperation(value="removeJob", notes="删除单个定时任务")
    public String removeJob(@RequestParam String id,
                                    @RequestParam String op,
                                    @RequestParam String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String openId = RequestUtils.getOpenId();
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            return JsonResponseUtil.badResult( BadResultCode.Date_Error.getCode(), BadResultCode.Date_Error.getRemark());
        }
        return ruleSchedule.removeJob(id , op, date, openId);
    }


    private String findFreeAllGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
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

    private String findGroupIdGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String findByGroupIdGetFallback(String groupId, Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String findByIdGetFallback(String id, Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String insertGetFallback(String id,
                                     String groupId,
                                     Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String updateGetFallback(String id,
                                     String groupId,
                                     String nickname,
                                     String status,
                                     Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String deleteByIdGetFallback(String id, Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String uploadGetFallback(String rule, HttpServletRequest request, Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String getJobGetFallback(Throwable e){
        e.printStackTrace();
        if ( e instanceof HystrixTimeoutException){
            log.error("Timeout");
            return "系统繁忙，请稍后";
        }
        log.error("Throwable info {}",e.getMessage());
        return "fail";
    }

    private String removeJobByOpenIdGetFallback(Throwable e){
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
