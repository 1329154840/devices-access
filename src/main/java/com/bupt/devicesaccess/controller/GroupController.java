package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.devicesaccess.dao.GroupRepository;
import com.bupt.devicesaccess.model.Group;
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

    @RequestMapping("/findAllGroup")
    public String findAllGroup(){
        return JSON.toJSONString(groupRepository.findAll());
    }

    @RequestMapping("/findByGroupId")
    public String findByGroupId(@RequestParam(value = "id") String id){
        return JSON.toJSONString(groupRepository.findByGroupId(id));
    }

    @RequestMapping("/insertNewGroup")
    public String insertNewGroup(@RequestParam(value = "deviceId") String deviceId){
        Group group = groupRepository.save(new Group(deviceId));
        log.info("New insert {}", group);
        return JSON.toJSONString(group);
    }

    @RequestMapping("/insertOldGroup")
    public String insertOldGroup(@RequestParam(value = "groupId") String groupId,
                                 @RequestParam(value = "deviceId") String deviceId){
        if(groupRepository.findByGroupId(groupId).isEmpty()){
            return JSON.toJSONString(null);
        }

        Group group = groupRepository.save( new Group( groupId, deviceId));
        log.info("New insert {}", group);
        return JSON.toJSONString(group);
    }



}
