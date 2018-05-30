/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : AbstractInterceptor.java
 * @Package : net.lizhaoweb.spring.mvc.core.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月26日
 * @Time : 下午10:57:27
 */
package net.lizhaoweb.spring.mvc.core.interceptor;

import net.lizhaoweb.common.util.base.Constant;
import net.lizhaoweb.common.util.base.HttpUtil;
import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.StatusCode;
import net.lizhaoweb.spring.mvc.util.Constant.MVCCore;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>拦截器 - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractInterceptor extends HandlerInterceptorAdapter implements AsyncHandlerInterceptor {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * preHandle 方法是进行处理器拦截用的。
     * 顾名思义，该方法将在 Controller 处理之前进行调用。
     * <p/>
     * SpringMVC 中的 Interceptor 是链式的，可以同时存在多个 Interceptor，然后 SpringMVC 会根据声明的前后顺序一个接一个
     * 的执行，而且所有的 Interceptor 中的 preHandle 方法都会在 Controller 方法调用之前调用。
     * SpringMVC 的这种 Interceptor 链式结构也是可以进行中断的，这种中断方式是令 preHandle 的返回值为 false，当 preHandle
     * 的返回值为 false 的时候整个请求就结束了。
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result;
        try {
            this.buildPathVariable(request);
            result = (this.beforeMethodExecute(request, response, handler) && super.preHandle(request, response, handler));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            DataDeliveryWrapper<Object> dataDeliveryWrapper = new DataDeliveryWrapper<>(StatusCode.SERVER_INTERNAL_EXCEPTION, "拦截器出错了");
            HttpUtil.printJson(response, dataDeliveryWrapper);
            result = false;
        }
        return result;
    }

    /**
     * 这个方法只会在当前这个 Interceptor 的 preHandle 方法返回值为 true 时才会执行。
     * postHandle 是进行处理器拦截用的，它的执行时间是在处理器进行处理（也就是调用 Controller 的方法）之后执行，但是它会
     * 在 DispatcherServlet 进行视图的渲染之前执行，也就是说在这个方法中你可以对 ModelAndView 进行操作。
     * 这个方法的链式结构跟正常访问的方向是相反的，也就是说先声明的 Interceptor 的该方法反而会后调用，这跟 Struts2 里面
     * 的拦截器的执行过程有点像，只是 Struts2 里面的 intercept 方法中要手动的调用 ActionInvocation 的 invoke 方法，
     * Struts2 中调用 ActionInvocation 的 invoke 方法就是调用下一个 Interceptor 或者是调用 action，然后要在 Interceptor
     * 之前调用的内容都写在调用 invoke 之前，要在 Interceptor 之后调用的内容都写在调用 invoke 方法之后。
     * {@inheritDoc}
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
        this.afterMethodExecute(request, response, handler, modelAndView);
    }

    /**
     * 该方法是需要当前对应的 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。
     * 该方法将在整个请求完成之后（也就是 DispatcherServlet 渲染了视图）执行。
     * 这个方法的主要作用是清理资源，当然这个方法也只能在当前这个 Interceptor 的 preHandle 方法的返回值为 true 时才会执行。
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     * 获取路径参数的值。
     *
     * @param request          请求对象
     * @param pathVariableName 路径参数名
     * @return 返回路径参数的值列表。
     */
    protected List<String> getPathVariableList(HttpServletRequest request, String pathVariableName) {
        List<String> result = null;
        @SuppressWarnings("unchecked")
        Map<String, List<String>> pathVariableMap = (Map<String, List<String>>) request.getAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP);
        if (pathVariableMap != null) {
            result = pathVariableMap.get(pathVariableName);
        }
        return result;
    }

    /**
     * 获取路径参数的值。
     *
     * @param request          请求对象
     * @param pathVariableName 路径参数名
     * @return 返回路径参数的值数组。
     */
    protected String[] getPathVariableArray(HttpServletRequest request, String pathVariableName) {
        String[] result = null;
        @SuppressWarnings("unchecked")
        Map<String, List<String>> pathVariableMap = (Map<String, List<String>>) request.getAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP);
        if (pathVariableMap != null) {
            List<String> list = pathVariableMap.get(pathVariableName);
            if (list != null) {
                result = list.toArray(new String[0]);
            }
        }
        return result;
    }

    /**
     * 获取路径参数的值。
     *
     * @param request          请求对象
     * @param pathVariableName 路径参数名
     * @return 返回路径参数的值列表的第一个。
     */
    protected String getPathVariable(HttpServletRequest request, String pathVariableName) {
        String result = null;
        @SuppressWarnings("unchecked")
        Map<String, List<String>> pathVariableMap = (Map<String, List<String>>) request.getAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP);
        if (pathVariableMap != null) {
            List<String> list = pathVariableMap.get(pathVariableName);
            if (list != null && list.size() > 0) {
                result = list.get(0);
            }
        }
        return result;
    }

    /**
     * 方法执行之前操作。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return boolean
     * @throws Exception 异常
     */
    protected abstract boolean beforeMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

    /**
     * 方法执行之后操作。
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param handler      处理器
     * @param modelAndView 视图模型
     * @return boolean
     * @throws Exception 异常
     */
    protected abstract void afterMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception;

    // 构建路径参数对象。
    private void buildPathVariable(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> pathVariableMap = (Map<String, List<String>>) request.getAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP);
        if (pathVariableMap == null) {
            pathVariableMap = new HashMap<>();
        } else {
            pathVariableMap.clear();
        }

        // 获取路径参数名
        String bestMatchingPatternAttribute = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String bestMatchingPatternAttributeCopy = bestMatchingPatternAttribute;
//        Pattern pattern = Pattern.compile("(?i)(?<=\\{)([^/\\{\\}]+)(?=\\})");
        Pattern pattern = Pattern.compile(Constant.Regex.CURLYBRACE_IN);
        Matcher matcher = pattern.matcher(bestMatchingPatternAttribute);
        List<String> pathVariableNameList = new ArrayList<>();
        while (matcher.find()) {
            String searchField = matcher.group();
            if (StringUtils.isNotEmpty(searchField)) {
                bestMatchingPatternAttributeCopy = bestMatchingPatternAttributeCopy.replace(String.format("{%s}", searchField), "(.+)");
                pathVariableNameList.add(searchField);
            }
        }

        // 获取路径参数值
        if (pathVariableNameList.size() > 0) {
            String pathWithinHandlerMappingAttribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            Pattern pathVariablePattern = Pattern.compile(bestMatchingPatternAttributeCopy);
            Matcher pathVariableMatcher = pathVariablePattern.matcher(pathWithinHandlerMappingAttribute);
            if (pathVariableMatcher.find()) {
                for (int index = 0; index < pathVariableNameList.size(); index++) {
                    String pathVariableName = pathVariableNameList.get(index);
                    List<String> pathVariableValueList = pathVariableMap.get(pathVariableName);
                    if (pathVariableValueList == null) {
                        pathVariableValueList = new ArrayList<>();
                    }
                    String pathVariableValue = pathVariableMatcher.group(index + 1);
                    pathVariableValueList.add(pathVariableValue);
                    pathVariableMap.put(pathVariableName, pathVariableValueList);
                }
            }
        }
        request.setAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP, pathVariableMap);
    }
}
