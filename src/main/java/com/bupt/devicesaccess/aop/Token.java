package com.bupt.devicesaccess.aop;

import java.lang.annotation.*;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-13
 * Time: 00:20
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */

/**
 * 方法前校验token，调取远端redis数据，接口由用户模块提供，restful接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {
    /**
     *  是否启用
     * @return
     */
    boolean value() default true;
}
