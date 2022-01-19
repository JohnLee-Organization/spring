/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.plugin.mybatis.executor.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 09:37
 */
package net.lizhaoweb.spring.plugin.mybatis.executor.impl;

import lombok.Setter;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>执行器 [实现] - SQL 脚本文件</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SQLScriptURLExecutor extends SQLScriptAbstractExecutor {

    @Setter
    private String url;
    @Setter
    private String driver;
    @Setter
    private String username;
    @Setter
    private String password;
    @Setter
    private List<String> sqlScriptURLList;

    @Setter
    private String charsetName = "UTF-8";
    @Setter
    private PrintWriter logPrintWriter;
    @Setter
    private boolean autoCommit = false;
    @Setter
    private boolean sendFullScript = true;


    @Override
    public void setSQLScript(String sqlScript) {
        if (this.sqlScriptURLList == null) {
            this.sqlScriptURLList = new ArrayList<String>();
        } else {
            this.sqlScriptURLList.clear();
        }
        this.sqlScriptURLList.add(sqlScript);
    }

    @Override
    public void setSQLScriptList(List<String> sqlScriptList) {
        this.sqlScriptURLList = sqlScriptList;
    }

    @Override
    public void execute() {
        Connection conn = null;
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, username, password);
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(Charset.forName(charsetName)); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(logPrintWriter);//设置是否输出日志
            runner.setAutoCommit(autoCommit);
            runner.setSendFullScript(sendFullScript);
            if (sqlScriptURLList != null && sqlScriptURLList.size() > 0) {
                for (String sqlScriptURL : sqlScriptURLList) {
                    runner.runScript(Resources.getUrlAsReader(sqlScriptURL));
                }
            }
            runner.closeConnection();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.closeConnection(conn);
        }
    }
}
