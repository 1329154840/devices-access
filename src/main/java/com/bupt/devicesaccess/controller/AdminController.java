package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.bupt.devicesaccess.utils.RequestUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-17
 * Time: 09:11
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */

/**
 * 管理员操作
 */
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    DeviceRepository deviceRepository;

    /**
     * 查询所有device
     * @return
     */
    @Token
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ApiOperation(value="findAll", notes="查询所有device")
    public String findAll(){
        return JsonResponseUtil.ok(deviceRepository.findAll());
    }
}
