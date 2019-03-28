package com.bupt.devicesaccess.model;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-26
 * Time: 18:34
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
public class RuleJobsVO {
    private String jobDetailName;
    private String jobStartTime;
    private String groupName;

    public String getJobDetailName() {
        return jobDetailName;
    }

    public void setJobDetailName(String jobDetailName) {
        this.jobDetailName = jobDetailName;
    }


    public String getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
