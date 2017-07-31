/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : PropertyConfigurer.java
 * @Package : net.lizhaoweb.spring.mvc.config
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月26日
 * @Time : 下午11:05:24
 */
package net.lizhaoweb.spring.mvc.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * <h1>配置 - 配置文件加载器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

    final Logger logger = LoggerFactory.getLogger(PropertyConfigurer.class);

    private static final String API_URL_FORMAT = "COM_DAASBANK_OPENSTACK_SWIFT_API_URL_FORMAT";
    private static final String API_URL_PARAMETERS = "COM_DAASBANK_OPENSTACK_SWIFT_API_URL_PARAMETERS";
    private static final String API_REQUEST_METHOD = "COM_DAASBANK_OPENSTACK_SWIFT_API_REQUEST_METHOD";
    private static final String API_REQUEST_HEADERS = "COM_DAASBANK_OPENSTACK_SWIFT_API_REQUEST_HEADERS";

    /**
     * 属性对象
     */
    private static Properties properties;

    /**
     * 属性 Map
     */
    private static Map<String, String> ctxPropertiesMap;

    /**
     * 解析后的属性 Map
     */
    private static Map<String, Map<String, String>> propertiesMap;

    static {
        ctxPropertiesMap = new HashMap<String, String>();
        propertiesMap = new HashMap<String, Map<String, String>>();
    }

    /**
     * Visit each bean definition in the given bean factory and attempt to
     * replace ${...} property placeholders with values from the given
     * properties.
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        properties = props;
        // load properties to ctxPropertiesMap
        analyzeAllProperties();
    }

    /**
     * 获取配置文件的属性值。
     *
     * @param key 配置文件中的键。
     * @return 返回配置文件中的属性值。
     */
    public static String getPropertyValue(String key) {
        return ctxPropertiesMap.get(key);
    }

    /**
     * 获取键为key的值中，url-format的值。
     *
     * @param key 配置文件中的键。
     * @return 返回键为key的值中url-format的值。
     * @throws URISyntaxException 异常
     */
    public static final String getPropertyURLFormat(String key) throws URISyntaxException {
        String url = null;
        Map<String, String> propertyMap = getPropertyConfig(key);
        if (propertyMap != null) {
            url = propertyMap.get(API_URL_FORMAT);
        }
        return url;
    }

    /**
     * 获取键为key的值中，url-parameters的值。
     *
     * @param key 配置文件中的键。
     * @return 返回键为key的值中，url-parameters的值。
     * @throws URISyntaxException 异常
     */
    public static final String getPropertyURLParameters(String key) throws URISyntaxException {
        String url = null;
        Map<String, String> propertyMap = getPropertyConfig(key);
        if (propertyMap != null) {
            url = propertyMap.get(API_URL_PARAMETERS);
        }
        return url;
    }

    /**
     * 获取键为key的值中，request-method的值。
     *
     * @param key 配置文件中的键。
     * @return 返回键为key的值中，request-method的值。
     * @throws URISyntaxException 异常
     */
    public static final String getPropertyRequestMethod(String key) throws URISyntaxException {
        String requestMethod = null;
        Map<String, String> propertyMap = getPropertyConfig(key);
        if (propertyMap != null) {
            requestMethod = propertyMap.get(API_REQUEST_METHOD);
        }
        return requestMethod;
    }

    /**
     * 获取键为key的值中，request-header-names的值。
     *
     * @param key 配置文件中的键。
     * @return 返回键为key的值中，request-header-names的值。
     * @throws URISyntaxException 异常
     */
    public static final String getPropertyRequestHeaders(String key) throws URISyntaxException {
        String requestHeaders = null;
        Map<String, String> propertyMap = getPropertyConfig(key);
        if (propertyMap != null) {
            requestHeaders = propertyMap.get(API_REQUEST_HEADERS);
        }
        return requestHeaders;
    }

    // 解析所有的配置属性。
    private static final void analyzeAllProperties() {
        Set<Entry<Object, Object>> entrySet = properties.entrySet();
        for (Entry<Object, Object> entry : entrySet) {
            String key = (String) entry.getKey();
            String value = properties.getProperty(key);
            ctxPropertiesMap.put(key, value);
            Map<String, String> propertyMap = _getPropertyConfig(value);
            propertiesMap.put(key, propertyMap);
        }
    }

    // 获取属性配置。
    private static Map<String, String> _getPropertyConfig(String propertyValue) {
        Map<String, String> propertyMap = null;
        if (StringUtils.isNotEmpty(propertyValue)) {
            String[] args = propertyValue.split(";");
            if (args != null && args.length > 0) {
                propertyMap = new HashMap<String, String>();
                for (String arg : args) {
                    if (StringUtils.isNotEmpty(arg)) {
                        if (arg.startsWith("request-method=")) {
                            String value = arg.trim().substring("request-method=".length());
                            propertyMap.put(API_REQUEST_METHOD, value);
                        } else if (arg.startsWith("request-header-names=")) {
                            String value = arg.trim().substring("request-header-names=".length());
                            propertyMap.put(API_REQUEST_HEADERS, value);
                        } else if (arg.startsWith("url-format=")) {
                            String value = arg.trim().substring("url-format=".length());
                            propertyMap.put(API_URL_FORMAT, value);
                        } else if (arg.startsWith("url-parameters=")) {
                            String value = arg.trim().substring("url-parameters=".length());
                            propertyMap.put(API_URL_PARAMETERS, value);
                        } else {
                        }
                    } else {
                    }
                }
            }
        }
        return propertyMap;
    }

    // 获取键为key的属性MAP。
    private static final Map<String, String> getPropertyConfig(String key) throws URISyntaxException {
        Map<String, String> propertyMap = null;
        if (propertiesMap != null) {
            propertyMap = propertiesMap.get(key);
        }
        return propertyMap;
    }
}
