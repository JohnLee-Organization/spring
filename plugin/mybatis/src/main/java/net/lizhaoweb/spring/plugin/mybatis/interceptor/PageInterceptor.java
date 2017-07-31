/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring Plugin Mybatis
 * @Title : PageInterceptor.java
 * @Package : net.lizhaoweb.spring.plugin.mybatis.interceptor
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年7月12日
 * @Time : 下午5:10:56
 */
package net.lizhaoweb.spring.plugin.mybatis.interceptor;

import net.lizhaoweb.spring.mvc.model.Page;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * <h1>Mybatis拦截器 - 分页</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年7月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor {

    /**
     * 拦截
     *
     * @param invocation
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // 当前环境 MappedStatement，BoundSql，及sql取得
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        String originalSql = this.formatSql(boundSql.getSql().trim());
        Object parameterObject = boundSql.getParameterObject();

        // Page对象获取，“信使”到达拦截器！
        Page page = this.searchPageWithXpath(parameterObject, ".", "page", "*/page");

        if (page != null) {
            // Page对象存在的场合，开始分页处理
            String countSql = this.getCountSql(originalSql);
            Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            PreparedStatement countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
            DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, countBS);
            parameterHandler.setParameters(countStmt);
            ResultSet rs = countStmt.executeQuery();
            int totpage = 0;
            if (rs.next()) {
                totpage = rs.getInt(1);
            }
            rs.close();
            countStmt.close();
            connection.close();

            // 分页计算
            page.setTotalRecord(totpage);

            // 对原始Sql追加limit
            int offset = (page.getPageNo() - 1) * page.getPageSize();
            StringBuffer sb = new StringBuffer();
            sb.append(originalSql).append(" LIMIT ").append(offset).append(",").append(page.getPageSize());
            BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sb.toString());
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;
        }
        return invocation.proceed();

    }

    /**
     * 根据给定的xpath查询Page对象
     */
    private Page searchPageWithXpath(Object o, String... xpaths) {
        JXPathContext context = JXPathContext.newContext(o);
        Object result;
        for (String xpath : xpaths) {
            try {
                result = context.selectSingleNode(xpath);
            } catch (JXPathNotFoundException e) {
                continue;
            }
            if (result instanceof Page) {
                return (Page) result;
            }
        }
        return null;
    }

    /**
     * 复制MappedStatement对象
     */
    private MappedStatement copyFromMappedStatement(MappedStatement mappedStatement, SqlSource newSqlSource) {
        Builder builder = new Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), newSqlSource, mappedStatement.getSqlCommandType());

        builder.resource(mappedStatement.getResource());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.statementType(mappedStatement.getStatementType());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        // builder.keyProperty(mappedStatement.getKeyPropertys());
        builder.timeout(mappedStatement.getTimeout());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());

        return builder.build();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 根据原Sql语句获取对应的查询总记录数的Sql语句
     */
    private String getCountSql(String sql) {
        String sqltemp = sql.toLowerCase();
        int fromIndex = sqltemp.indexOf(" from ");
        fromIndex = fromIndex < 0 ? 0 : fromIndex + 6;
        int orderIndex = sqltemp.lastIndexOf(" order by ");
        orderIndex = orderIndex < 0 ? sqltemp.length() : orderIndex;
        String subSQL = sqltemp.substring(fromIndex, orderIndex);
        return String.format("SELECT COUNT(*) FROM %s", subSQL);
    }

    /**
     * 格式化SQL语句。
     *
     * @param sql
     * @return
     */
    private String formatSql(String sql) {
        if (sql == null) {
            return sql;
        }
        while (sql.indexOf("\n") > -1) {
            sql = sql.replace("\n", " ");
        }
        while (sql.indexOf("\r") > -1) {
            sql = sql.replace("\r", " ");
        }
        while (sql.indexOf("\t") > -1) {
            sql = sql.replace("\t", " ");
        }
        while (sql.indexOf("  ") > -1) {
            sql = sql.replace("  ", " ");
        }
        return sql;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {
    }

    public class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
