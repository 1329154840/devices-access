package com.bupt.devicesaccess.config;

import com.bupt.devicesaccess.filter.TokenFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-21
 * Time: 18:08
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Component
public class WebConfig {
    @Bean
    public FilterRegistrationBean registerTokenFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TokenFilter filter = new TokenFilter();
        registrationBean.setFilter(filter);
        List<String> urls = new ArrayList<>();
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
