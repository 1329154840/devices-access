package com.bupt.devicesaccess.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 22:08
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Data
@PrimaryKeyClass
public class GroupKey {
    @PrimaryKeyColumn(value = "group_id",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String groupId;
    @PrimaryKeyColumn(value = "device_id",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
    private String deviceId;

    public GroupKey(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public GroupKey(String deviceId) {
        this.groupId = UUID.randomUUID().toString();
        this.deviceId = deviceId;
    }

    public GroupKey(){
    }
}
