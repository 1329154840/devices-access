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

    Token_Access_Fail(2,"token校验失败"),

    Device_Is_Null(3,"device不存在"),

    Group_Is_Null(4,"group不存在"),

    System_Error(5,"系统异常");


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
