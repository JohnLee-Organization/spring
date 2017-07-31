/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : SolrUtil.java
 * @Package : net.lizhaoweb.spring.mvc.solr
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年5月6日
 * @Time : 上午9:25:21
 */
package net.lizhaoweb.spring.solr;

import net.lizhaoweb.common.util.base.Constant.Regex;
import net.lizhaoweb.common.util.base.ReflectUtil;
import net.lizhaoweb.common.util.base.StringUtil;
import net.lizhaoweb.spring.mvc.model.Page;
import net.lizhaoweb.spring.mvc.search.AbstractSearch;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>工具 - Solr</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年5月6日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SolrUtil {

    protected static Logger logger = LoggerFactory.getLogger(SolrUtil.class);

    /**
     * 通过 SolrDocument 构建 JAVA BEAN。
     *
     * @param document Solr Document。
     * @param mapping  Solr Document 与 JAVA BEAN 的映射。
     * @param <T>      泛型
     * @return T
     * @throws NullPointerException      空指针异常
     * @throws ClassNotFoundException    没有找到类异常
     * @throws InstantiationException    实例化异常
     * @throws IllegalAccessException    访问异常
     * @throws NoSuchMethodException     没有方法异常
     * @throws SecurityException         安全异常
     * @throws IllegalArgumentException  参数异常
     * @throws InvocationTargetException 实例目标异常
     * @throws NoSuchFieldException      没有字段异常
     */
    public static <T> T solrDocument2Entity(SolrDocument document, SolrMapping mapping) throws NullPointerException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        if (document == null) {
            throw new NullPointerException("SolrDocument is null");
        }
        if (mapping == null) {
            throw new NullPointerException("SolrMapping is null");
        }
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) Class.forName(mapping.getClassName());
        Object object = clazz.newInstance();
        List<SolrField> solrFields = mapping.getSolrFields();
        for (SolrField solrField : solrFields) {
            Field field = ReflectUtil.getField(clazz, solrField.getBeanName());
            if (logger.isDebugEnabled()) {
                logger.debug("Field " + solrField.getBeanName());
            }
            if (field == null) {
                throw new NullPointerException(String.format("Field (%s) not found", solrField.getBeanName()));
            }
            Class<?> fieldType = field.getType();
            // 构造set方法名 setId
            String dynamicSetMethod = dynamicMethodName(solrField.getBeanName(), "set");
            String solrName = solrField.getSolrName();
            Object solrValue = document.getFieldValue(solrName);
            if (solrValue != null) {
                Method method = clazz.getMethod(dynamicSetMethod, fieldType);
                if (method == null) {
                    throw new NullPointerException(String.format("Method (%s) not found", dynamicSetMethod));
                }
                Class<?> realFieldType = ReflectUtil.baseTypeCast(fieldType);
                if (logger.isDebugEnabled()) {
                    logger.debug(realFieldType + "");
                }
                if (logger.isDebugEnabled()) {
                    logger.debug(solrValue + "");
                }
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("Field [name]%s  [type]%s  [value]%s", solrField.getBeanName(), realFieldType.getName(), solrValue));
                }
                method.invoke(object, realFieldType.cast(solrValue));
            }
        }
        return clazz.cast(object);
    }

    /**
     * 通过 SolrDocumentList 构建 JAVA BEAN列表。
     *
     * @param documentList Solr Document 列表。
     * @param mapping      Solr Document 与 JAVA BEAN 的映射。
     * @param <T>          泛型
     * @return List&lt;T&gt;
     */
    public static <T> List<T> solrDocument2Entity(SolrDocumentList documentList, SolrMapping mapping) throws NullPointerException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        if (documentList == null) {
            throw new NullPointerException("SolrDocumentList is null");
        }
        List<T> list = new ArrayList<T>();
        for (SolrDocument document : documentList) {
            T bean = solrDocument2Entity(document, mapping);
            list.add(bean);
        }
        return list;
    }

    /**
     * 设置分页
     *
     * @param solrQuery Solr查询对象
     * @param page      分布对象
     */
    public static void setPageInSolrQuery(SolrQuery solrQuery, Page page) {
        if (solrQuery == null) {
            throw new NullPointerException("SolrQuery is null");
        }
        int start = 0, rows = Integer.MAX_VALUE;
        if (page != null) {
            start = (page.getPageNo() - 1) * page.getPageSize();
            rows = page.getPageSize();
        }
        solrQuery.setStart(start).setRows(rows);
    }

    /**
     * 设置查询字段
     *
     * @param solrQuery Solr查询对象
     * @param mapping   solr 映射对象。
     * @param object
     */
    public static void setFieldsInSolrQuery(SolrQuery solrQuery, SolrMapping mapping, Object object) {
        if (solrQuery == null) {
            throw new NullPointerException("SolrQuery is null");
        }
        if (mapping == null) {
            throw new NullPointerException("SolrMapping is null");
        }
        String methodName = ReflectUtil.getRuntimeMethodName(object);
        MethodConfig methodConfig = mapping.getMethodConfig().get(methodName);
        if (methodConfig != null && methodConfig.getFields() != null) {
            solrQuery.setFields(methodConfig.getFields().toArray(new String[0]));
        }
    }

    /**
     * 设置排序
     *
     * @param solrQuery Solr 查询对象
     * @param mapping   solr 映射对象。
     * @param object
     */
    public static void setSortsInSolrQuery(SolrQuery solrQuery, SolrMapping mapping, Object object) {
        if (solrQuery == null) {
            throw new NullPointerException("SolrQuery is null");
        }
        if (mapping == null) {
            throw new NullPointerException("SolrMapping is null");
        }
        String methodName = ReflectUtil.getRuntimeMethodName(object);
        MethodConfig methodConfig = mapping.getMethodConfig().get(methodName);
        if (methodConfig != null && methodConfig.getSorts() != null) {
            Set<Entry<String, ORDER>> sortEntrys = methodConfig.getSorts().entrySet();
            int index = 0;
            for (Entry<String, ORDER> sortEntry : sortEntrys) {
                if (sortEntry != null) {
                    if (index == 0) {
                        solrQuery.setSort(sortEntry.getKey(), sortEntry.getValue());
                    } else {
                        solrQuery.addSort(sortEntry.getKey(), sortEntry.getValue());
                    }
                    index++;
                }
            }
        }
    }

    /**
     * 设置必要的查询条件
     *
     * @param solrQuery Solr查询对象
     * @param mapping   solr 映射对象。
     * @param object
     * @param search
     */
    public static void setQueryInSolrQuery(SolrQuery solrQuery, SolrMapping mapping, Object object, AbstractSearch search) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (solrQuery == null) {
            throw new NullPointerException("SolrQuery is null");
        }
        if (mapping == null) {
            throw new NullPointerException("SolrMapping is null");
        }
        String methodName = ReflectUtil.getRuntimeMethodName(object);
        MethodConfig methodConfig = mapping.getMethodConfig().get(methodName);
        if (methodConfig != null && StringUtils.isNotEmpty(methodConfig.getPrerequisite())) {
            String prerequisite = StringUtil.deleteSpacesAndNewLine(methodConfig.getPrerequisite());
            while (prerequisite.indexOf(" : $[") > -1) {
                prerequisite = prerequisite.replace(" : $[", ":$[");
            }
            while (prerequisite.indexOf(":$[ ") > -1) {
                prerequisite = prerequisite.replace(":$[ ", ":$[");
            }
            while (prerequisite.indexOf(" ]") > -1) {
                prerequisite = prerequisite.replace(" ]", "]");
            }
            // Pattern pattern =
            // Pattern.compile("(?i)(?<=:\\$\\[)([^\\$\\[\\]]+)(?=\\])");
            Pattern pattern = Pattern.compile(Regex.DOLLAR_SQUAREBRACKET_IN);
            Matcher matcher = pattern.matcher(prerequisite);
            Class<?> searchClass = search.getClass();
            while (matcher.find()) {
                String searchField = matcher.group();
                Field field = ReflectUtil.getField(searchClass, searchField);
                String searchFieldValue = null;
                if (boolean.class.equals(field.getType())) {
                    searchFieldValue = dynamicMethodName(searchField, "is");
                } else {
                    searchFieldValue = dynamicMethodName(searchField, "get");
                }
                Method method = searchClass.getMethod(searchFieldValue);
                Object value = method.invoke(search);
                String realValue = "*";
                if (value != null) {
                    realValue = value + "";
                }
                prerequisite = prerequisite.replace(String.format("$[%s]", searchField), realValue);
            }
            solrQuery.setQuery(prerequisite);
        } else {
            solrQuery.setQuery("*:*");
        }
    }

    /**
     * @param mapping solr 映射对象。
     * @param object
     * @param search
     * @param <T>
     * @return List
     */
    public static <T> List<T> getList(SolrMapping mapping, Object object, AbstractSearch search) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SolrServerException, NullPointerException, ClassNotFoundException, InstantiationException, NoSuchFieldException {
        SolrQuery solrQuery = new SolrQuery();
        setQueryInSolrQuery(solrQuery, mapping, object, search);// 设置必要的查询条件
        setFieldsInSolrQuery(solrQuery, mapping, object);// 设置返回字段
        setSortsInSolrQuery(solrQuery, mapping, object);// 设置排序
        setPageInSolrQuery(solrQuery, search.getPage());// 设置分页

        SolrServer server = new HttpSolrServer(mapping.getSolrPath());
        QueryResponse response = server.query(solrQuery);

        SolrDocumentList documentList = response.getResults();
        List<T> list = solrDocument2Entity(documentList, mapping);
        return list;
    }

    // 构建方法
    private static String dynamicMethodName(String fieldName, String prefix) {
        return String.format("%s%s", prefix, StringUtil.capitalize(fieldName));
    }
}
