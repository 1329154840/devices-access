package com.bupt.devicesaccess.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.model.RuleJobsVO;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-26
 * Time: 18:33
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
@Component
public class RuleSchedule{
    @Autowired
    Scheduler scheduler;
    /**
     * 规则上传
     * @param uid
     * @param rule
     * @return
     */
    public String adapter(String uid, JSONObject rule){
        JSONArray data = rule.getJSONArray("data");
        String type = rule.getString("type");
        for(int i=0; i<data.size(); i++){
            JSONObject singleRule = data.getJSONObject(i);
            String id = singleRule.getString("id");
            String op = singleRule.getString("op");
            Date date = singleRule.getDate("date");
            if ( !addJob( id, op, uid, date )){
                return JsonResponseUtil.badResult( BadResultCode.Rule_Upload_Error.getCode(), BadResultCode.Rule_Upload_Error.getRemark() + String.format(",第%d可能重复", i+1));
            }
        }
        return JsonResponseUtil.ok("规则上传成功");
    }

    /**
     * 给单个传感器添加一个任务
     * @param id
     * @param operate
     * @param uid
     * @param date
     * @Return boolean
     */
    private boolean addJob(String id,String operate, String uid, Date date ){
        String jobName = id + "/" + operate + "/" + date.toString();
        JobDetail jobDetail = JobBuilder.newJob(RuleJob.class)
                .withIdentity(jobName , uid).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity( jobName, uid)
                .startAt(date)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        }catch (Exception e){
            log.error("[定时任务添加异常] jobName:[{}] cause:[{}]", jobName, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 输出即将执行的定时任务
     * @return
     */
    public String printJob(){
        List<RuleJobsVO> ruleJobsVOList = new ArrayList<>();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String groupName: triggerGroupNames){
                /**
                 *组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                 */
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    /**
                     * 通过triggerKey在scheduler中获取trigger对象
                     */
                    Trigger trigger =scheduler.getTrigger(triggerKey);
                    /**
                     * 获取trigger拥有的Job
                     */
                    JobKey jobKey = trigger.getJobKey();
                    JobDetailImpl jobDetailImpl = (JobDetailImpl) scheduler.getJobDetail(jobKey);
                    /**
                     * 组装页面需要显示的数据
                     */
                    RuleJobsVO ruleJobsVO = new RuleJobsVO();
                    ruleJobsVO.setGroupName(groupName);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ruleJobsVO.setJobStartTime(sdf.format(trigger.getStartTime()));
                    ruleJobsVO.setJobDetailName(jobDetailImpl.getName());
                    ruleJobsVOList.add(ruleJobsVO);
                }
            }
        }catch (Exception e){
            log.error("printJob异常 {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.ok(ruleJobsVOList);
    }



}
