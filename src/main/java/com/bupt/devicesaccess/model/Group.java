package com.bupt.devicesaccess.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 17:55
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Data
@Table("Group")

public class Group {

    @PrimaryKey
    private GroupKey groupKey;

    public Group(String deviceId){
       this.groupKey = new GroupKey(deviceId);
    }

    public Group(String groupId, String deviceId) {
        this.groupKey = new GroupKey( groupId, deviceId);
    }
    public Group() {
    }
}
