package com.bupt.devicesaccess.aop.aspect;

import com.bupt.devicesaccess.aop.Token;
import com.bupt.devicesaccess.utils.BadResultCode;
import com.bupt.devicesaccess.utils.JsonResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

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
    @Pointcut("@annotation(token)")
    public void service(Token token){
    }

    @Around("service(token)")
    public  Object Interceptor(ProceedingJoinPoint joinPoint, Token token){
        Object result = null;
        if(token.value()){
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
            Cookie[] cookies = request.getCookies();
           if(cookies!=null){
               log.info("cookies not null");
               for (Cookie cookie:cookies){
                   if (cookie.getName().equals("token") && cookie.getValue().equals("123")){
                       result ="ok";
                       log.info("token 验证成功");
                   }
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
}
