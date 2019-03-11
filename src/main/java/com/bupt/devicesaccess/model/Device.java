package com.bupt.devicesaccess.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-11
 * Time: 11:26
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Data
@Table("Device")
public class Device {
    @PrimaryKey
    private String id;
    @Column("customer_id")
    private Integer customId;
    @Column("tenant_id")
    private Integer tenantId;
    private String model;
    private String name;
    private String nickname;
    private String status;

    public Device(Integer tenantId,Integer customId,String model, String name) {
        this.id = UUID.randomUUID().toString();
        this.customId = customId;
        this.tenantId = tenantId;
        this.model = model;
        this.name = name;
        this.status = "关机";
    }

    public Device(String id, Integer customId, Integer tenantId, String model, String name, String nickname, String status) {
        this.id = id;
        this.customId = customId;
        this.tenantId = tenantId;
        this.model = model;
        this.name = name;
        this.nickname = nickname;
        this.status = status;
    }
    public Device() {
    }
}
