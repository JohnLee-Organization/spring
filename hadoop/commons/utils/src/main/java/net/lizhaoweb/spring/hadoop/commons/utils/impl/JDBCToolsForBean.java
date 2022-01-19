/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 10:06
 */
package net.lizhaoweb.spring.hadoop.commons.utils.impl;

import net.lizhaoweb.spring.hadoop.commons.utils.IJDBCToolsForBean;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>工具类 [实现] - Mysql</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class JDBCToolsForBean extends JDBCTools implements IJDBCToolsForBean {

    /**
     * 有参构造。
     *
     * @param driver   驱动
     * @param url      连接地址
     * @param username 用户名
     * @param password 密码
     */
    public JDBCToolsForBean(String driver, String url, String username, String password) {
        super(driver, url, username, password);
    }

    /**
     * {@inheritDoc}
     */
    public List<Map<String, Object>> toList(String sql, Object... params) {
        try {
            ResultSet resultSet = this.executeQuery(sql, params);
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    String columnName = resultSetMetaData.getColumnName(columnIndex + 1);
                    if (columnName == null || columnName.trim().length() < 1) {
                        continue;
                    }
                    Object columnValue = resultSet.getObject(columnName);
                    map.put(columnName, columnValue);
                }
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> toList(Class<T> clazz, String sql, Object... params) {
        if (sql == null) {
            throw new IllegalArgumentException("The argument[sql] is null");
        }
        return this.toList(this.getConnection(), clazz, sql, params);
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> toList(Connection connection, Class<T> clazz, String sql, Object... params) {
        if (sql == null) {
            throw new IllegalArgumentException("The argument[sql] is null");
        }
        PreparedStatement preparedStatement = this.getPreparedStatement(connection, sql, params);
        return this.toList(preparedStatement, clazz);
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> toList(PreparedStatement preparedStatement, Class<T> clazz) {
        if (preparedStatement == null) {
            throw new IllegalArgumentException("The argument[preparedStatement] is null");
        }
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            return this.toList(resultSet, clazz);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> toList(ResultSet resultSet, Class<T> clazz) {
        if (resultSet == null) {
            throw new IllegalArgumentException("The argument[resultSet] is null");
        }
        if (clazz == null) {
            throw new IllegalArgumentException("The argument[clazz] is null");
        }
        try {
            List<T> list = new ArrayList<T>();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                T bean = clazz.newInstance();
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    String columnName = resultSetMetaData.getColumnName(columnIndex + 1);
                    if (columnName == null || columnName.trim().length() < 1) {
                        continue;
                    }
                    Field field = this.getField(clazz, columnName);
                    if (field == null) {
                        continue;
                    }
                    String methodName = String.format("set%s", StringUtils.capitalize(columnName));
                    Method method = this.getFieldSetMethod(clazz, field, methodName);
                    if (method == null) {
                        continue;
                    }
                    this.executeMethod(bean, resultSet, columnName, field, method);
                }
                list.add(bean);
            }
            return list;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    // 获取属性的 Set 方法
    private Method getFieldSetMethod(Class<?> clazz, Field field, String methodName) throws NoSuchMethodException {
        Class<?> fieldType = field.getType();
        Method method = clazz.getMethod(methodName, fieldType);
        if (method == null) {
            method = this.getFieldSetMethod(clazz, field, methodName);
        }
        return method;
    }

    // 获取属性
    private Field getField(Class<?> clazz, String columnName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(columnName);
        if (field == null) {
            field = this.getField(clazz.getSuperclass(), columnName);
        }
        return field;
    }

    // 执行方法
    private <T> void executeMethod(T bean, ResultSet resultSet, String columnName, Field field, Method method) throws SQLException, IllegalAccessException, InvocationTargetException {
//        if (byte.class.equals(field.getType()) || Byte.class.equals(field.getType())) {
//            Byte columnValue = (Byte) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (short.class.equals(field.getType()) || Short.class.equals(field.getType())) {
//            Short columnValue = (Short) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
//            Integer columnValue = (Integer) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
//            Long columnValue = (Long) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
//            Float columnValue = (Float) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (double.class.equals(field.getType()) || Double.class.equals(field.getType())) {
//            Double columnValue = (Double) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (char.class.equals(field.getType()) || Character.class.equals(field.getType())) {
//            Character columnValue = (Character) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else if (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
//            Boolean columnValue = (Boolean) resultSet.getObject(columnName);
//            method.invoke(bean, columnValue);
//        } else {
        Object columnValue = resultSet.getObject(columnName);
        method.invoke(bean, columnValue);
//        }
    }
}
