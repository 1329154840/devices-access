package com.bupt.devicesaccess.controller;

import com.alibaba.fastjson.JSON;
import com.bupt.devicesaccess.dao.GroupRepository;
import com.bupt.devicesaccess.model.Group;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

/**
 * 将设备分组存入，组内的crud操作（组内无需更新）
 */
@RestController
@Slf4j
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    RestTemplate restTemplate;

    /**
     * 查询所有组
     * @return
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询所有组")
    public String findAll(){
        return JsonResponseUtil.ok(groupRepository.findAll());
    }

    /**
     * 按groupId查询查整组记录 list<Group>
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/findByGroupId", method = RequestMethod.GET)
    @ApiOperation(value="findByGroupId", notes="按groupId查询查整组记录")
    public String findByGroupId(@RequestParam(value = "groupId") String groupId){
        return JsonResponseUtil.ok(groupRepository.findByGroupId(groupId));
    }

    /**
     * 插入新的组
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/insertNew", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="insertNew", notes="插入新的组")
    public String insertNew(@RequestParam(value = "deviceId") String deviceId){
        Group group = groupRepository.save(new Group(deviceId));
        log.info("insertNew {}", group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 插入已存在的组
     * @param groupId
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/insertOld", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="insertOld", notes="插入已存在的组")
    public String insertOld(@RequestParam(value = "groupId") String groupId,
                                 @RequestParam(value = "deviceId") String deviceId){
        if(groupRepository.findByGroupId(groupId).isEmpty()){
            return JsonResponseUtil.badResult("group不存在");
        }

        Group group = groupRepository.save( new Group( groupId, deviceId));
        log.info("insertOld {}", group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 删除组内单条记录
     * @param groupId
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/deleteByPrimaryKey", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="deleteByPrimaryKey", notes="删除组内单条记录")
    public String deleteByPrimaryKey(@RequestParam(value = "groupId") String groupId,
                              @RequestParam(value = "deviceId") String deviceId){
        Group group = groupRepository.findByPrimaryKey(groupId,deviceId);
        if (group == null){
            return JsonResponseUtil.badResult("group不存在");
        }
        log.info("deleteSingleGroup {}", group);
        groupRepository.delete(group);
        return JsonResponseUtil.ok(group);
    }

    /**
     * 根据GroupId删除整组
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/deleteByGroupId", method = {RequestMethod.GET,RequestMethod.POST})
    @ApiOperation(value="deleteByGroupId", notes="根据GroupId删除整组")
    public String deleteByGroupId(@RequestParam(value = "groupId") String groupId){
        if (groupRepository.findByGroupId(groupId).isEmpty()){
            return JsonResponseUtil.badResult("group不存在");
        }
        groupRepository.deleteGroupByGroupId(groupId);
        log.info("deleteGroupList {}", groupId);
        return JsonResponseUtil.ok(groupId);
    }


    @RequestMapping(value = "/restful", method = RequestMethod.GET)
    public String restful(){
        String url = "http://DEVICES-ACCESS/findAllGroup";
        String json = restTemplate.getForObject(url, String.class);
        Map<String,Object> result = JSON.parseObject(json);
        List<Group> groupList = (List<Group>)result.get("data");
        log.info("restful {}",groupList);
        return JsonResponseUtil.ok(groupList);
    }


}
