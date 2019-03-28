package com.bupt.devicesaccess.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-26
 * Time: 11:27
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext1)
            throws BeansException {
        applicationContext = applicationContext1;
    }

    /**
     * 从当前IOC获取bean
     *
     * @param id
     *            bean的id
     * @return
     */
    public static Object getBean(String id) {
        return applicationContext.getBean(id);
    }

    public static <T> T getBean(String id, Class<T> clazz) {
        return applicationContext.getBean(id, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
