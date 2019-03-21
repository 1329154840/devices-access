package com.bupt.devicesaccess.filter;

import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-21
 * Time: 18:00
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Slf4j
public class TokenFilter implements Filter {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response  = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String result = "";
        if (!uri.contains("favicon") && !uri.equals("/")){
            String role = "";
            if(uri.contains("/user")){
                log.info("user");
                role ="user";
            }
            if (uri.contains("/admin")){
                log.info("admin");
                role ="admin";
            }
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (Cookie cookie:cookies){
                    if ( cookie.getName().equals("token") && checkTokenByRestFul( cookie.getValue(), role)){
                        result ="ok";
                    }
                }
            }
            if(!"ok".equals(result)){
                result = JsonResponseUtil.badResult(BadResultCode.Token_Access_Fail.getCode(), BadResultCode.Token_Access_Fail.getRemark());
                response.setContentType("text/html;charset=UTF-8");
                response.sendError(403,result);
            }

        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /**
     * 通过restful调取account服务的检查token接口(redis)
     * @param token
     * @param role
     * @return Boolean
     */
    private Boolean checkTokenByRestFul(String token, String role){
        String buffer[] = token.split("-");
        String result ="";
        String url ="";
        if (buffer.length!=3){
            return Boolean.FALSE;
        }
        if (role.equals("user")){
            url = String.format("http://ACCOUNT/device/check?uid=%s&type=%d&uuid=%s",buffer[0],1,buffer[2]);
        }
        if (role.equals("admin")){
            url = String.format("http://ACCOUNT/device/check?uid=%s&type=%d&uuid=%s",buffer[0],0,buffer[2]);
        }
        try {
            result = restTemplate.getForObject(url, String.class);
            log.info("{}",result);
        } catch (Exception e){
            log.error("{}",e.getMessage());
            return Boolean.FALSE;
        }
        if (result.equals("true")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("TokenFilter");
    }

    @Override
    public void destroy() {

    }
}
