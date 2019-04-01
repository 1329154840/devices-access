package com.bupt.devicesaccess.utils;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-16
 * Time: 23:53
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
public class RequestUtils {
    /**
     * 通过cookie拿token中的OpenId
     * @return
     */
    public static Integer getOpenId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    System.out.println(cookie.getValue());
                    String[] buffer = cookie.getValue().split("-");

                    return Integer.valueOf(buffer[0]);
                }
            }
        }
        return 0;
    }
}
