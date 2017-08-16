/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:07
 */
package net.lizhaoweb.spring.hadoop.commons.utils.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.lizhaoweb.spring.hadoop.commons.utils.IJDBCTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * <h1>工具类 [实现] [抽象] - JDBC</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RequiredArgsConstructor
public class JDBCTools implements IJDBCTools {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    // 定义数据库的驱动信息
    @NonNull
    private String driver;

    // 定义访问数据库的地址
    @NonNull
    private String url;

    // 表示定义数据库的用户名
    @NonNull
    private String username;

    // 定义数据库的密码
    @NonNull
    private String password;


    private Connection connection;// 定义数据库的链接
    private PreparedStatement preparedStatement; // 定义sql语句的执行对象
    private ResultSet resultSet;// 定义查询返回的结果集合


    /**
     * {@inheritDoc}
     */
    public Connection getConnection() {
        try {
            Class.forName(this.driver); // 注册驱动
            this.connection = DriverManager.getConnection(this.url, this.username, this.password); // 获取连接
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.connection;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Connection getConnectionForSingle() {
        synchronized (this) {
            String jdbcPoolKey = String.format("%s%s%s%s", this.driver, this.url, this.username, this.password);
            Connection connectionSingle = JDBCFactory.getConnection(jdbcPoolKey);
            if (connectionSingle == null) {
                connectionSingle = this.getConnection();
                JDBCFactory.addConnection(jdbcPoolKey, connectionSingle);
            }
            return connectionSingle;
        }
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement getPreparedStatement(String sql, Object... params) {
        Connection connection = this.getConnection();
        return this.getPreparedStatement(connection, sql, params);
    }

    /**
     * {@inheritDoc}
     */
    public PreparedStatement getPreparedStatement(Connection connection, String sql, Object... params) {
        if (sql == null) {
            throw new IllegalArgumentException("The argument[sql] is null");
        }
        try {
            this.preparedStatement = connection.prepareStatement(sql);
            if (params != null && params.length > 0) {
                for (int index = 0, argIndex = 1; index < params.length; index++) {
                    this.preparedStatement.setObject(argIndex++, params[index]);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return this.preparedStatement;
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String sql, Object... params) {
        try {
            return this.getPreparedStatement(sql, params).executeQuery();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int executeUpdate(String sql, Object... params) {
        try {
            return this.getPreparedStatement(sql, params).executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean execute(String sql, Object... params) {
        try {
            return this.getPreparedStatement(sql, params).execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void releaseConnection() {
        this.close(this.resultSet);
        this.close(this.preparedStatement);
        this.close(this.connection);
    }

    // 释放资源
    private void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        autoCloseable = null;
    }
}
