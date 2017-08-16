/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:30
 */
package net.lizhaoweb.spring.hadoop.commons.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * <h1>工具类 [接口] - JDBC For Beam</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IJDBCToolsForBean extends IJDBCTools {

    /**
     * 将 {@link ResultSet} 对象转换为 JAVA 对象
     *
     * @param resultSet {@link ResultSet} 对象
     * @param clazz     JAVA 对象的类型
     * @param <T>       java 泛型
     * @return List
     */
    <T> List<T> toList(ResultSet resultSet, Class<T> clazz);

    /**
     * 执行查询操作
     *
     * @param preparedStatement SQL 语句预处理器
     * @param clazz             JAVA 对象的类型
     * @return List
     */
    <T> List<T> toList(PreparedStatement preparedStatement, Class<T> clazz);

    /**
     * 执行查询操作
     *
     * @param connection 连接对象
     * @param clazz      JAVA 对象的类型
     * @param sql        SQL 语句
     * @param params     SQL 语句参数
     * @param <T>        java 泛型
     * @return List
     */
    <T> List<T> toList(Connection connection, Class<T> clazz, String sql, Object... params);


    /**
     * 执行查询操作
     *
     * @param sql    sql语句
     * @param params 执行参数
     * @return List
     */
    <T> List<T> toList(Class<T> clazz, String sql, Object... params);

    /**
     * 执行查询操作
     *
     * @param sql    sql语句
     * @param params 执行参数
     * @return List
     */
    List<Map<String, Object>> toList(String sql, Object... params);
}
