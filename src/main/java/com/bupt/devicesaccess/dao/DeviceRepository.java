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
    String SELECT = "SELECT id,customer_id,group_id,model,name,nickname,status FROM device ";

    @Query(SELECT + "WHERE customer_id = :custom_id ALLOW FILTERING")
    List<Device> findFreeAll(@Param("custom_id") Integer customId);

    @Query(SELECT + "WHERE customer_id = :custom_id ALLOW FILTERING")
    List<Device> findByCustomId(@Param("custom_id") Integer customId);

    @Query(SELECT + "WHERE customer_id = :custom_id And group_id = :group_id ALLOW FILTERING")
    List<Device> findByCustomIdAndGroupId(@Param("custom_id") Integer customId, @Param("group_id") String groupId);

    @Query("SELECT DISTINCT group_id FROM device WHERE customer_id = :custom_id ALLOW FILTERING")
    List<String> findGroupId(@Param("custom_id") Integer customId);

    @Query("UPDATE device SET status = :status WHERE id = :id ")
    void updateById(@Param("id") String id, @Param("status") String status);
}
