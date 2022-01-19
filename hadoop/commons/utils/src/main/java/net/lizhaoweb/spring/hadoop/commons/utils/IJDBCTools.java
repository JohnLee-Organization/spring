/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 10:04
 */
package net.lizhaoweb.spring.hadoop.commons.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <h1>工具类 [接口] - JDBC</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IJDBCTools {

    /**
     * 获取数据库连接
     *
     * @return 数据库连接
     */
    Connection getConnection();

    /**
     * 获取数据库连接单例
     *
     * @return 数据库连接
     */
    Connection getConnectionForSingle();

    /**
     * 获取 SQL 预处理对象。
     *
     * @param sql    SQL 语句
     * @param params SQL 语句的参数
     * @return PreparedStatement
     */
    PreparedStatement getPreparedStatement(String sql, Object... params);

    /**
     * 获取 SQL 预处理对象。
     *
     * @param connection 连接
     * @param sql        SQL 语句
     * @param params     SQL 语句的参数
     * @return PreparedStatement
     */
    PreparedStatement getPreparedStatement(Connection connection, String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param connection 连接
     * @param sql        SQL 语句
     * @param params     SQL 语句的参数
     * @return boolean
     */
    boolean execute(Connection connection, String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param sql    SQL 语句
     * @param params SQL 语句的参数
     * @return boolean
     */
    boolean execute(String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param connection 连接
     * @param sql        SQL 语句
     * @param params     SQL 语句的参数
     * @return int
     */
    int executeUpdate(Connection connection, String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param sql    SQL 语句
     * @param params SQL 语句的参数
     * @return int
     */
    int executeUpdate(String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param connection 连接
     * @param sql        SQL 语句
     * @param params     SQL 语句的参数
     * @return ResultSet
     */
    ResultSet executeQuery(Connection connection, String sql, Object... params);

    /**
     * 执行 SQL 语句
     *
     * @param sql    SQL 语句
     * @param params SQL 语句的参数
     * @return ResultSet
     */
    ResultSet executeQuery(String sql, Object... params);

    /**
     * 释放资源
     */
    void releaseConnection();
}
