package com.bupt.devicesaccess.dao;

import com.bupt.devicesaccess.model.Device;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 11:24
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
public interface DeviceRepository extends CrudRepository<Device,String> {

}
