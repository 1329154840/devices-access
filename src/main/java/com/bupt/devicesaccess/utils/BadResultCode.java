package com.bupt.devicesaccess.utils;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-13
 * Time: 07:15
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */

public enum BadResultCode {
    /**
     * BadResultCode
     */
    Token_Access_Fail(2,"Toekn 认证失败，你没有权限"),

    Device_Is_Null(3,"device不存在"),

    Group_Is_Null(4,"group不存在"),

    System_Error(5,"系统异常"),

    Rule_Upload_Error(6,"规则上传失败"),

    Rule_Json_Error(7, "json解析错误"),

    Date_Error(8, "date解析错误"),

    Job_Is_Null(9 , "定时任务不存在");


    BadResultCode(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public Integer getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    private Integer code;

    private String remark;
}
