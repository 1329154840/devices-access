package com.bupt.devicesaccess.dao;

import com.bupt.devicesaccess.model.Group;
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
 * Time: 18:01
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
public interface GroupRepository extends CrudRepository<Group,String> {
    @Query(" SELECT group_id,device_id FROM group WHERE group_id = :group_id  ")
    List<Group> findByGroupId(@Param("group_id") String groupId);

    @Query(" SELECT group_id,device_id FROM group WHERE group_id = :group_id AND  device_id = :device_id ")
    Group findByPrimaryKey(@Param("group_id") String groupId,@Param("device_id") String deviceId);

    @Query(" Delete From group WHERE group_id = :group_id ")
    void deleteGroupByGroupId(@Param("group_id") String groupId);
}
