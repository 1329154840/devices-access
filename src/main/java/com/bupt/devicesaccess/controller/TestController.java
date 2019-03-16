package com.bupt.devicesaccess.controller;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-16
 * Time: 08:06
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@RestController
@RequestMapping("/")
public class TestController {
    @Token
    @RequestMapping(value = "/",method = RequestMethod.GET)
    @ApiOperation(value="mainUrl", notes="这只是一个测试controller调用的接口，没有任何的业务逻辑")
    public String mainUrl(){
        return JsonResponseUtil.ok("devices-access");
    }

}
