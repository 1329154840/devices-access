package com.bupt.devicesaccess.dao;

import com.bupt.devicesaccess.model.Device;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
    String SELECT = "SELECT id,open_id,group_id,model,name,nickname,status FROM device ";

    @Query(SELECT + "WHERE open_id = '-1' ALLOW FILTERING")
    List<Device> findFreeAll();

    @Query(SELECT + "WHERE open_id = :open_id ALLOW FILTERING")
    List<Device> findByOpenId(@Param("open_id") String openId);

    @Query(SELECT + "WHERE open_id = :open_id And group_id = :group_id ALLOW FILTERING")
    List<Device> findByOpenIdAndGroupId(@Param("open_id") String openId, @Param("group_id") String groupId);


    @Query("UPDATE device SET status = :status WHERE id = :id ")
    void updateById(@Param("id") String id, @Param("status") String status);
}
