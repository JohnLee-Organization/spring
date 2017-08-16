/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:42
 */
package net.lizhaoweb.spring.hadoop.commons.utils.impl;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h1>工厂 - JDBC</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class JDBCFactory {

    public static final String NAMESPACE_DEFAULT = "JOHN.LEE-NAMESPACE_DEFAULT";

    public static final boolean CONTINUE_ON_ERROR_DEFAULT = false;

    /**
     * JDBC 连接池
     */
    private static Map<String, Map<String, Connection>> CONNECTION_POOL;

    // 静态代码块做初始化操作
    static {
        CONNECTION_POOL = new ConcurrentHashMap<>();
        CONNECTION_POOL.put(NAMESPACE_DEFAULT, new ConcurrentHashMap<String, Connection>());
    }

    /**
     * 把 JDBC 连接加入池中。
     *
     * @param key        键
     * @param connection JDBC 连接
     * @return boolean
     */
    public static boolean addConnection(String key, Connection connection) {
        return addConnection(NAMESPACE_DEFAULT, key, connection);
    }

    /**
     * 把 JDBC 连接加入池中。
     *
     * @param namespace  命名空间
     * @param key        键
     * @param connection JDBC 连接
     * @return boolean
     */
    public static boolean addConnection(String namespace, String key, Connection connection) {
        return addConnection(namespace, key, connection, CONTINUE_ON_ERROR_DEFAULT);
    }

    /**
     * 把 JDBC 连接加入池中。
     *
     * @param namespace       命名空间
     * @param key             键
     * @param connection      JDBC 连接
     * @param continueOnError 异常时，是否继续
     * @return boolean
     */
    public static boolean addConnection(String namespace, String key, Connection connection, boolean continueOnError) {
        if (continueOnError && CONNECTION_POOL == null) {
            CONNECTION_POOL = new ConcurrentHashMap<>();
        }
        Map<String, Connection> connectionPool = CONNECTION_POOL.get(namespace);
        if (continueOnError && connectionPool == null) {
            connectionPool = new ConcurrentHashMap<>();
        }
        Connection connectionSave = connectionPool.put(key, connection);
        if (connectionSave == null) {
            return false;
        }
        Map<String, Connection> connectionPoolSave = CONNECTION_POOL.put(namespace, connectionPool);
        return connectionPoolSave != null;
    }

    /**
     * 从池中获取 JDBC 连接。
     *
     * @param key 键
     * @return Connection
     */
    public static Connection getConnection(String key) {
        return getConnection(NAMESPACE_DEFAULT, key);
    }

    /**
     * 从池中获取 JDBC 连接。
     *
     * @param namespace 命名空间
     * @param key       键
     * @return Connection
     */
    public static Connection getConnection(String namespace, String key) {
        return getConnection(namespace, key, CONTINUE_ON_ERROR_DEFAULT);
    }

    /**
     * 从池中获取 JDBC 连接。
     *
     * @param namespace       命名空间
     * @param key             键
     * @param continueOnError 异常时，是否继续
     * @return Connection
     */
    public static Connection getConnection(String namespace, String key, boolean continueOnError) {
        if (continueOnError && CONNECTION_POOL == null) {
            return null;
        }
        Map<String, Connection> connectionPool = CONNECTION_POOL.get(namespace);
        if (continueOnError && connectionPool == null) {
            return null;
        }
        return connectionPool.get(key);
    }

    /**
     * 从池中删除 JDBC 连接。
     *
     * @param key 键
     * @return boolean
     */
    public static boolean removeConnection(String key) {
        return removeConnection(NAMESPACE_DEFAULT, key);
    }

    /**
     * 从池中删除 JDBC 连接。
     *
     * @param namespace 命名空间
     * @param key       键
     * @return boolean
     */
    public static boolean removeConnection(String namespace, String key) {
        return removeConnection(namespace, key, CONTINUE_ON_ERROR_DEFAULT);
    }

    /**
     * 从池中删除 JDBC 连接。
     *
     * @param namespace       命名空间
     * @param key             键
     * @param continueOnError 异常时，是否继续
     * @return boolean
     */
    public static boolean removeConnection(String namespace, String key, boolean continueOnError) {
        if (continueOnError && CONNECTION_POOL == null) {
            return true;
        }
        Map<String, Connection> connectionPool = CONNECTION_POOL.get(namespace);
        if (continueOnError && connectionPool == null) {
            return true;
        }
        Connection connection = connectionPool.get(key);
        if (connection == null) {
            return true;
        }
        Connection connectionRemove = connectionPool.remove(key);
        return connectionRemove != null;
    }

    /**
     * 删除连接池
     *
     * @param namespace 命名空间
     * @return boolean
     */
    public static boolean removeConnectionPool(String namespace) {
        return removeConnectionPool(namespace, CONTINUE_ON_ERROR_DEFAULT);
    }

    /**
     * 删除连接池
     *
     * @param namespace       命名空间
     * @param continueOnError 异常时，是否继续
     * @return boolean
     */
    public static boolean removeConnectionPool(String namespace, boolean continueOnError) {
        if (continueOnError && CONNECTION_POOL == null) {
            return true;
        }
        Map<String, Connection> connectionPool = CONNECTION_POOL.get(namespace);
        if (continueOnError && connectionPool == null) {
            return true;
        }
        Map<String, Connection> connectionPoolRemove = CONNECTION_POOL.remove(namespace);
        return connectionPoolRemove != null;
    }

    /**
     * 清空命名空间中的连接池。
     *
     * @param namespace 命名空间
     */
    public static void cleanConnectionPool(String namespace) {
        cleanConnectionPool(namespace, CONTINUE_ON_ERROR_DEFAULT);
    }

    /**
     * 清空命名空间中的连接池。
     *
     * @param namespace       命名空间
     * @param continueOnError 异常时，是否继续
     */
    public static void cleanConnectionPool(String namespace, boolean continueOnError) {
        if (continueOnError && CONNECTION_POOL == null) {
            return;
        }
        Map<String, Connection> connectionPool = CONNECTION_POOL.get(namespace);
        if (continueOnError && connectionPool == null) {
            return;
        }
        connectionPool.clear();
    }
}
