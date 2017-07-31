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
import net.lizhaoweb.spring.mvc.util.Constant.MVCCore;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;
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
 * <h1>拦截器- 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class AbstractInterceptor extends HandlerInterceptorAdapter {

    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result;
        int code = 15000;
        String message = null;
        try {
            this.buildPathVariable(request);

            result = this.preMethodExecute(request, response, handler);
            if (result) {
                result = super.preHandle(request, response, handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
            code += HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            message = "拦截器出错了";
            DataDeliveryWrapper<Object> dataDeliveryWrapper = new DataDeliveryWrapper<Object>(code, message, null);
            HttpUtil.printJson(response, dataDeliveryWrapper);
            result = false;
        }
        return result;
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

    // 构建路径参数对象。
    private void buildPathVariable(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> pathVariableMap = (Map<String, List<String>>) request.getAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP);
        if (pathVariableMap == null) {
            pathVariableMap = new HashMap<String, List<String>>();
        } else {
            pathVariableMap.clear();
        }

        // 获取路径参数名
        String bestMatchingPatternAttribute = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String bestMatchingPatternAttributeCopy = bestMatchingPatternAttribute;
//        Pattern pattern = Pattern.compile("(?i)(?<=\\{)([^/\\{\\}]+)(?=\\})");
        Pattern pattern = Pattern.compile(Constant.Regex.CURLYBRACE_IN);
        Matcher matcher = pattern.matcher(bestMatchingPatternAttribute);
        List<String> pathVariableNameList = new ArrayList<String>();
        while (matcher.find()) {
            String searchField = matcher.group();
            if (StringUtils.isNotEmpty(searchField)) {
                bestMatchingPatternAttributeCopy = bestMatchingPatternAttributeCopy.replace(String.format("{%s}", searchField), "(.+)");
                pathVariableNameList.add(searchField);
            }
        }

        // 获取路径参数值
        if (pathVariableNameList != null && pathVariableNameList.size() > 0) {
            String pathWithinHandlerMappingAttribute = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            Pattern pathVariablePattern = Pattern.compile(bestMatchingPatternAttributeCopy);
            Matcher pathVariableMatcher = pathVariablePattern.matcher(pathWithinHandlerMappingAttribute);
            if (pathVariableMatcher.find()) {
                for (int index = 0; index < pathVariableNameList.size(); index++) {
                    String pathVariableName = pathVariableNameList.get(index);
                    List<String> pathVariableValueList = pathVariableMap.get(pathVariableName);
                    if (pathVariableValueList == null) {
                        pathVariableValueList = new ArrayList<String>();
                    }
                    String pathVariableValue = pathVariableMatcher.group(index + 1);
                    pathVariableValueList.add(pathVariableValue);
                    pathVariableMap.put(pathVariableName, pathVariableValueList);
                }
            }
        }
        request.setAttribute(MVCCore.APPLICATION_PATH_VARIABLE_MAP, pathVariableMap);
    }

    /**
     * 方法执行前操作。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return boolean
     * @throws Exception 异常
     */
    protected abstract boolean preMethodExecute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
