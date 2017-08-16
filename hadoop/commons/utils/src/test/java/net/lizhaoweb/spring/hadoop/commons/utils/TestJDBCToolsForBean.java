/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.utils
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:50
 */
package net.lizhaoweb.spring.hadoop.commons.utils;

import net.lizhaoweb.spring.hadoop.commons.utils.impl.JDBCToolsForBean;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年08月16日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class TestJDBCToolsForBean {

    private IJDBCToolsForBean jdbcToolsForBean;

    private String database;

    private String table;

    @Before
    public void init() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3307?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8&zeroDateTimeBehavior=convertToNull";
        String username = "root";
        String password = "123456";
        this.jdbcToolsForBean = new JDBCToolsForBean(driver, url, username, password);
        this.database = "test_db";
        this.table = "test_table";
    }

    @Test
    public void executeCreateDB() {
        String createDBSQL = String.format("CREATE DATABASE IF NOT EXISTS %s", this.database);
//        System.out.println(this.jdbcToolsForBean.execute(createDBSQL));
        System.out.println(this.jdbcToolsForBean.executeUpdate(createDBSQL));
    }

    @Test
    public void executeCreateTable() {
        String createTableSQL = String.format("CREATE TABLE IF NOT EXISTS %s.%s (id bigint(20),name varchar(48))", this.database, this.table);
//        System.out.println(this.jdbcToolsForBean.execute(createTableSQL));
        System.out.println(this.jdbcToolsForBean.executeUpdate(createTableSQL));
    }

    @Test
    public void insert() {
        String insertSQL = String.format("INSERT INTO %s.%s(id,name) VALUES(1,'name1'),(2,'name2')", this.database, this.table);
        System.out.println(this.jdbcToolsForBean.executeUpdate(insertSQL));
    }

    @Test
    public void update() {
        String insertSQL = String.format("UPDATE %s.%s SET name='name3' WHERE id=2", this.database, this.table);
        System.out.println(this.jdbcToolsForBean.executeUpdate(insertSQL));
    }

    @Test
    public void delete() {
        String insertSQL = String.format("DELETE FROM %s.%s WHERE id=1", this.database, this.table);
        System.out.println(this.jdbcToolsForBean.executeUpdate(insertSQL));
    }

    @Test
    public void select() {
        String insertSQL = String.format("SELECT * FROM %s.%s", this.database, this.table);
        System.out.println(this.jdbcToolsForBean.executeQuery(insertSQL));
    }

    @Test
    public void executeDropTable() {
        String createTableSQL = String.format("DROP TABLE IF EXISTS %s.%s", this.database, this.table);
//        System.out.println(this.jdbcToolsForBean.execute(createTableSQL));
        System.out.println(this.jdbcToolsForBean.executeUpdate(createTableSQL));
    }

    @Test
    public void executeDropDB() {
        String createDBSQL = String.format("DROP DATABASE IF EXISTS %s", this.database);
//        System.out.println(this.jdbcToolsForBean.execute(createDBSQL));
        System.out.println(this.jdbcToolsForBean.executeUpdate(createDBSQL));
    }
}
