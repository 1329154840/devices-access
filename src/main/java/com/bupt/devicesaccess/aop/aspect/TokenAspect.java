package com.bupt.devicesaccess.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import lombok.extern.slf4j.Slf4j;;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Description:  ---——require需求|ask问题|jira
 * Design :  ----the  design about train of thought 设计思路
 * User: yezuoyao
 * Date: 2019-03-13
 * Time: 00:58
 * Email:yezuoyao@huli.com
 *
 * @author yezuoyao
 * @since 1.0-SNAPSHOT
 */
@Aspect
@Component
@Slf4j
public class TokenAspect {
    @Autowired
    RestTemplate restTemplate;

    @Pointcut("@annotation(token)")
    public void service(Token token){
    }

    /**
     * 获取request，来得到cookie，通过restful校验token
     * @param joinPoint
     * @param token
     * @return
     */
    @Around("service(token)")
    public  Object Interceptor(ProceedingJoinPoint joinPoint, Token token){
        Object result = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie:cookies){
                if ( cookie.getName().equals("token") && checkTokenByRestFul( cookie.getValue(), token.value())){
                    result ="ok";
                    log.info("token 验证成功");
                }
            }
        }
        try{
            if("ok".equals(result)){
                // 一切正常的情况下，继续执行被拦截的方法
                result = joinPoint.proceed();
            }else {
                result = JsonResponseUtil.badResult(BadResultCode.Token_Access_Fail.getCode(), BadResultCode.Token_Access_Fail.getRemark());
            }
        }catch (Throwable e){
            result = JsonResponseUtil.badResult(BadResultCode.System_Error.getCode(), "发生异常："+e.getMessage());
        }
        return result;
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
            url = String.format("http://ACCOUNT/check?uid=%s&type=%d&uuid=%s",buffer[0],1,buffer[1]);
        }
        if (role.equals("admin")){
            url = String.format("http://ACCOUNT/check?uid=%s&type=%d&uuid=%s",buffer[0],0,buffer[1]);
        }
        try {
            result = restTemplate.getForObject(url, String.class);
        } catch (Exception e){
            log.error("{}",e.getMessage());
            return Boolean.FALSE;
        }
        if (result.equals("true")){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
