package com.bupt.devicesaccess.schedule;

import com.bupt.devicesaccess.dao.DeviceRepository;
import com.bupt.devicesaccess.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-26
 * Time: 18:38
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
public class RuleJob implements Job {
    @Autowired
    private DeviceRepository deviceRepository;

    /**
     * 修改对应设备status
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        String[] buffer = jobName.split("/");
        String id = buffer[0];
        String op = buffer[1];
        String printTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info("[job start] id: {} op: {} Date: {}", id, op, printTime);
        if(deviceRepository == null){
            deviceRepository = BeanUtil.getBean(DeviceRepository.class);
        }
        if (deviceRepository.existsById(id)){
            deviceRepository.updateById(id, op);
        }else {
            log.info("[job start] id不存在");
        }
    }
}
