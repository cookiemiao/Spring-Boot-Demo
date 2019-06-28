package com.qzc.qzc.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class WebLogAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.qzc.qzc.controller..*.getUser(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String methodname = joinPoint.getSignature().getName();
        // 记录下请求内容
        logger.info("METHOD_NAME : {}", methodname);
        logger.info("URL : {}", request.getRequestURL().toString());
        logger.info("HTTP_METHOD : {}", request.getMethod());
        logger.info("IP : {}", request.getRemoteAddr());
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String name = enu.nextElement();
            logger.info("输入参数名 : {} , 输入参数值 : {}", name, request.getParameter(name));
        }
        try {
            //通过完整类名反射加载类
            Class cla = joinPoint.getTarget().getClass();
            //获取首字母小写类名
            String simpleName = cla.getSimpleName();
            String firstLowerName = simpleName.substring(0, 1).toLowerCase()
                    + simpleName.substring(1);
            Object[] o = joinPoint.getArgs();
            //通过此方法去Spring容器中获取Bean实例
            Object obj = SpringContextUtil.getBean(firstLowerName, cla);
            Method m = obj.getClass().getMethod("getRawData", Object[].class);
            Object result = m.invoke(obj, new Object[]{o});
            Class cls = result.getClass();
            Field[] fields = cls.getDeclaredFields();
            logger.info("==============获取待修改的原始数据==================");
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                f.setAccessible(true);
                logger.info("原始数据属性名:{},原始数据属性值:{}", f.getName(), f.get(result));
            }
            logger.info("==================================================");
        } catch (Exception e) {
            logger.warn("取得原始数据失败！", e);
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(JoinPoint point, Object ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        // 处理完请求，返回内容
        logger.info("Status : " + response.getStatus());
    }
}