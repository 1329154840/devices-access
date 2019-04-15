package com.bupt.devicesaccess.schedule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bupt.devicesaccess.model.RuleJobsVO;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import com.bupt.devicesaccess.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Time;
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
    @Qualifier("mySchedule")
    @Autowired
    Scheduler scheduler;
    /**
     * 规则上传
     * @param rule
     * @return
     */
    public String adapter(JSONObject rule){
        JSONArray ruleArray = rule.getJSONArray("rule");
        for(int i = 0;i < ruleArray.size();i++){
            JSONObject data = JSONObject.parseObject(ruleArray.get(0).toString());
            String time = data.getString("date");

            Date date = new Date(Long.valueOf(time));

            log.info("date={}",date);
            //SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        try {
//            java.util.Date d = format.parse(time);
//            java.sql.Time time1 = new java.sql.Time(d.getTime());
//            date =time1;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

            String type = rule.getString("type");
            //for(int i=0; i<data.size(); i++){
            //JSONObject singleRule = data.getJSONObject(i);
            String id = data.getString("id");
            String op = data.getString("op");
            if ( !addJob( id, op, date )){
                return JsonResponseUtil.badResult( BadResultCode.Rule_Upload_Error.getCode(), BadResultCode.Rule_Upload_Error.getRemark() + String.format(",第%d可能重复", i+1));
            }
            //}
        }
        return JsonResponseUtil.ok("规则上传成功");
    }

    /**
     * 给单个传感器添加一个任务
     * @param id
     * @param operate
     * @param date
     * @Return boolean
     */
    private boolean addJob(String id,String operate, Date date ){
        String jobName = id + "/" + operate + "/" + date.toString();
        String openId = RequestUtils.getOpenId();
        log.info("{}", openId);
        JobDetail jobDetail = JobBuilder.newJob(RuleJob.class)
                .withIdentity(jobName , openId).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity( jobName, openId)
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
     * 输出所有定时任务
     * @return
     */
    public String printAllJob(){
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
            log.error("printAllJob异常 {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.ok(ruleJobsVOList);
    }

    /**
     * 输出个人定时任务
     * @return
     */
    public String printJobByOpenId(){
        List<RuleJobsVO> ruleJobsVOList = new ArrayList<>();
        String openId = RequestUtils.getOpenId();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String groupName: triggerGroupNames){
                /**
                 *组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                 */
                if(groupName.equals( openId )){
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
            }
        }catch (Exception e){
            log.error("printJobByCustomerId异常 {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.ok(ruleJobsVOList);
    }

    /**
     * 删除单个定时任务
     * @param id
     * @param operate
     * @param date
     */
    public String removeJob(String id,String operate, Date date, String openId) {
        String name = id + "/" + operate + "/" + date.toString();
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String groupName: triggerGroupNames) {
                /**
                 *组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                 */
                if (groupName.equals(openId)) {
                    GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                    //获取所有的triggerKey
                    Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                    for (TriggerKey triggerKey : triggerKeySet) {
                        if( name.equals(triggerKey.getName()) && openId.equals(triggerKey.getGroup())){
                            // 停止触发器
                            scheduler.pauseTrigger(triggerKey);
                            // 移除触发器
                            scheduler.unscheduleJob(triggerKey);
                            // 删除任务
                            scheduler.deleteJob(JobKey.jobKey(name,openId));
                            return JsonResponseUtil.ok(name);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("removeJob {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.badResult( BadResultCode.Job_Is_Null.getCode(), BadResultCode.Job_Is_Null.getRemark());
    }

    /**
     * 删除用户所有定时任务
     * @return
     */
    public String removeJobByOpenId(String openId) {
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String groupName: triggerGroupNames){
                /**
                 *组装group的匹配，为了模糊获取所有的triggerKey或者jobKey
                 */
                if(groupName.equals( openId )){
                    GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                    //获取所有的triggerKey
                    Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                    for (TriggerKey triggerKey : triggerKeySet) {
                        // 停止触发器
                        scheduler.pauseTrigger( triggerKey);
                        // 移除触发器
                        scheduler.unscheduleJob( triggerKey);
                        // 删除任务
                        scheduler.deleteJob( JobKey.jobKey( triggerKey.getName(), triggerKey.getGroup()));
                    }
                }
            }
        }catch (Exception e){
            log.error("removeJobByOpenId {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.ok();
    }

    /**
     * 删除所有定时任务
     * @return
     */
    public String removeAllJob(){
        try {
            List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
            for(String groupName: triggerGroupNames){
                GroupMatcher groupMatcher = GroupMatcher.groupEquals(groupName);
                //获取所有的triggerKey
                Set<TriggerKey> triggerKeySet = scheduler.getTriggerKeys(groupMatcher);
                for (TriggerKey triggerKey : triggerKeySet) {
                    // 停止触发器
                    scheduler.pauseTrigger( triggerKey);
                    // 移除触发器
                    scheduler.unscheduleJob( triggerKey);
                    // 删除任务
                    scheduler.deleteJob( JobKey.jobKey( triggerKey.getName(), triggerKey.getGroup()));
                }
            }
        }catch (Exception e){
            log.error("removeAllJob {}", e.getMessage());
            return JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), BadResultCode.System_Error.getRemark());
        }
        return JsonResponseUtil.ok();
    }
}
