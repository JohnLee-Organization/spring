/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.plugin.mybatis.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @Time : 10:42
 */
package net.lizhaoweb.spring.plugin.mybatis.interceptor;

import net.lizhaoweb.common.util.base.ReflectUtil;
import net.lizhaoweb.spring.plugin.mybatis.type.ResultMapField;
import net.lizhaoweb.spring.plugin.mybatis.type.ResultMapObject;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @notes Created on 2016年07月19日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Intercepts(@Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {Statement.class}))
public class MapObjectsInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //通过invocation获取代理的目标对象
        Object target = invocation.getTarget();
        if (!(target instanceof DefaultResultSetHandler)) {
            return invocation.proceed();
        }
        Object mappedStatement = ReflectUtil.getFieldValue(target, "mappedStatement");
        if (!(mappedStatement instanceof MappedStatement)) {
            return invocation.proceed();
        }
        List<ResultMap> resultMaps = ((MappedStatement) mappedStatement).getResultMaps();
        if (resultMaps == null || resultMaps.size() != 1) {
            return invocation.proceed();
        }
        ResultMap resultMap = resultMaps.get(0);
        if (resultMap == null) {
            return invocation.proceed();
        }
        Object[] args = invocation.getArgs();
        if (args == null || args.length < 1) {
            return invocation.proceed();
        }
        try {
            Statement statement = (Statement) args[0];
            return this.handleResultSet(invocation, (DefaultResultSetHandler) target, statement, resultMap);
        } catch (Exception e) {
            return invocation.proceed();
        }
    }

    @Override
    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    @Override
    public void setProperties(Properties props) {
    }

    private Object handleResultSet(Invocation invocation, ResultSetHandler resultSetHandler, Statement statement, ResultMap resultMap) throws Throwable {
        List<Object> list = resultSetHandler.handleResultSets(statement);
        if (ResultMapObject.class.equals(resultMap.getType())) {
            //通过Statement获取到当前的结果集，对其进行处理，并返回对应的处理结果
            return this.handleResultSetMapObjects(statement.getResultSet());
        } else if (ResultMapField.class.equals(resultMap.getType())) {
            //通过Statement获取到当前的结果集，对其进行处理，并返回对应的处理结果
            return this.handleResultSetMapFields(statement.getResultSet());
        }
        //如果没有进行拦截处理，则执行默认逻辑
        return invocation.proceed();
    }

    /**
     * 处理结果集
     *
     * @param resultSet
     * @return
     */
    private Object handleResultSetMapObjects(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        //定义用于存放Key-Value的Map
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
            //把每一行对应的Key和Value存放到Map中
            while (resultSet.next()) {
//                resultSet.getCursorName();
//                Object key = resultSet.getObject(keyField);
//                Object value = resultSet.getObject(valueField);
//                map.put(key, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }
        return map;
    }

    /**
     * 处理结果集
     *
     * @param resultSet
     * @return
     */
    private Object handleResultSetMapFields(ResultSet resultSet) {
        if (resultSet == null) {
            return null;
        }
        //定义用于存放Key-Value的Map
        Map<Object, Object> map = new HashMap<Object, Object>();
        try {
            //把每一行对应的Key和Value存放到Map中
            while (resultSet.next()) {
//                Object key = resultSet.getObject(keyField);
//                Object value = resultSet.getObject(valueField);
//                map.put(key, value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(resultSet);
        }
        return map;
    }

    /**
     * 关闭ResultSet
     *
     * @param resultSet 需要关闭的ResultSet
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {

        }
    }
}
