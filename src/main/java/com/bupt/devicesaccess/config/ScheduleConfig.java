package com.bupt.devicesaccess.config;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-26
 * Time: 18:09
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Configuration
public class ScheduleConfig {
    /**
     * 创建调度器
     * @return
     * @throws SchedulerException
     * @throws InterruptedException
     */
    @Bean("mySchedule")
    public Scheduler createScheduler ()throws SchedulerException, InterruptedException{
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        return scheduler;
    }
}
