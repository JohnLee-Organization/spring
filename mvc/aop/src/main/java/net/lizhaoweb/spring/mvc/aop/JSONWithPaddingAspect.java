/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.aop
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 11:35
 */
package net.lizhaoweb.spring.mvc.aop;

import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

/**
 * <H1>切面 - JsonP</H1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年05月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Aspect
@Component
public class JSONWithPaddingAspect extends AbstractAspect {

    @Pointcut("@annotation(net.lizhaoweb.spring.mvc.aop.annotation.JSONWithPadding)")
    public void jsonWithPaddingPointcut() {
    }

    /**
     * 切点前执行。
     * 执行顺序： 2
     *
     * @param joinPoint 切点信息对象
     */
    @Before("jsonWithPaddingPointcut()")
    public void beforeExec(JoinPoint joinPoint) {
    }

    /**
     * 切点后执行。
     * 执行顺序： 3
     *
     * @param joinPoint    切点信息对象
     * @param returnObject 切点返回值
     * @return 返回的处理结果
     */
    @AfterReturning(pointcut = "jsonWithPaddingPointcut()", returning = "returnObject")
    public Object afterExec(JoinPoint joinPoint, Object returnObject) {
        return returnObject;
    }

    /**
     * 环绕切点执行。
     * 执行顺序： 1
     *
     * @param proceedingJoinPoint 切点信息对象
     * @return 返回的处理结果
     */
    @Around("jsonWithPaddingPointcut()")
    public Object aroundExec(ProceedingJoinPoint proceedingJoinPoint) {
        String callback = request.getParameter("callback");
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            result = new DataDeliveryWrapper<>(StatusCode.SERVER_INTERNAL_EXCEPTION, "AOP Around JsonP 执行异常");
        }

        // 不是跨域，返回结果
        if (StringUtil.isBlank(callback)) {
            return result;
        }

        // 是跨域，需要支持 JsonP 调用
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
