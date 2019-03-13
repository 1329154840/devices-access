package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.dao.GroupRepository;
import com.bupt.devicesaccess.model.Device;
import com.bupt.devicesaccess.model.Group;
import com.bupt.devicesaccess.model.GroupKey;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 18:03
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@RestController
@Slf4j
public class GroupController {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 查询所有组
     * @return
     */
    @RequestMapping("/findAllGroup")
    public String findAllGroup(){
        return JsonResponseUtil.ok(groupRepository.findAll());
    }

    /**
     * 按groupId查询list的group
     * @param groupId
     * @return
     */
    @RequestMapping("/findByGroupId")
    public String findByGroupId(@RequestParam(value = "groupId") String groupId){
        return JsonResponseUtil.ok(groupRepository.findByGroupId(groupId));
    }

    /**
     * 插入新的组
     * @param deviceId
     * @return
     */
    @RequestMapping("/insertNewGroup")
    public String insertNewGroup(@RequestParam(value = "deviceId") String deviceId){
        Group group = groupRepository.save(new Group(deviceId));
        log.info("New insert {}", group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 插入已存在的组
     * @param groupId
     * @param deviceId
     * @return
     */
    @RequestMapping("/insertOldGroup")
    public String insertOldGroup(@RequestParam(value = "groupId") String groupId,
                                 @RequestParam(value = "deviceId") String deviceId){
        if(groupRepository.findByGroupId(groupId).isEmpty()){
            return JsonResponseUtil.badResult("group不存在");
        }

        Group group = groupRepository.save( new Group( groupId, deviceId));
        log.info("Old insert {}", group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 删除组内单条记录
     * @param groupId
     * @param deviceId
     * @return
     */
    @RequestMapping("/deleteGroupByPrimaryKey")
    public String deleteGroupByPrimaryKey(@RequestParam(value = "groupId") String groupId,
                              @RequestParam(value = "deviceId") String deviceId){
        Group group = groupRepository.findByPrimaryKey(groupId,deviceId);
        if (group == null){
            return JsonResponseUtil.badResult("group不存在");
        }
        log.info("delete {}", group);
        groupRepository.delete(group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 根据GroupId删除整组
     * @param groupId
     * @return
     */
    @RequestMapping("deleteGroupByGroupId")
    public String deleteGroupByGroupId(@RequestParam(value = "groupId") String groupId){
        if (groupRepository.findByGroupId(groupId).isEmpty()){
            return JsonResponseUtil.badResult("group不存在");
        }
        groupRepository.deleteGroupByGroupId(groupId);
        log.info("delete group {}", groupId);
        return JsonResponseUtil.ok(groupId);
    }


    @RequestMapping("restful")
    public String restful(){
        String url = "http://DEVICES-ACCESS/findAllGroup";
        String json = restTemplate.getForObject(url, String.class);
        Map<String,Object> result = JSON.parseObject(json);
        List<Group> groupList = (List<Group>)result.get("data");
        log.info("restful {}",groupList);
        return JsonResponseUtil.ok(groupList);
    }


}
