/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : hadoop
 * @Package : com.littlehotspot.hadoop.mr.export.excel.hive
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 20:49
 */
package net.lizhaoweb.spring.hadoop.commons.mrcce.db;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>读写器 - 简单数据</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年06月20日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class SimpleDataWritable implements Writable, DBWritable {

    private static final char FIELD_DELIMITER = 0x0001;

    private List<String> dataNameList = new ArrayList<>();
    private Map<String, DBColumn<Object>> dataMap = new HashMap<>();

    public String toString() {
        StringBuilder toString = new StringBuilder();
        if (dataNameList != null && dataMap != null) {
            int dataListSize = dataNameList.size();
            int dataMapSize = dataMap.size();
            if (dataListSize != dataMapSize) {
                throw new IllegalStateException("Data size wrong");
            }
            if (dataListSize > 0 && dataMapSize > 0) {
                for (int index = 0; index < dataListSize; index++) {
                    String columnName = dataNameList.get(index);
                    DBColumn<Object> dbColumn = dataMap.get(columnName);
                    toString.append(dbColumn.getColumnValue());
                    if (index + 1 < dataListSize) {
                        toString.append(FIELD_DELIMITER);
                    }
                }
            }
        }
        System.out.println(toString);
        System.out.println();
        return toString.toString();
    }

    @Override
    public void readFields(ResultSet result) throws SQLException {
//        System.out.println();
        dataNameList.clear();
        ResultSetMetaData resultSetMetaData = result.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            String columnLabel = resultSetMetaData.getColumnLabel(column);
            String columnClassName = resultSetMetaData.getColumnClassName(column);
//            String tableName = resultSetMetaData.getTableName(column);
            String columnName = resultSetMetaData.getColumnName(column);
            String columnTypeName = resultSetMetaData.getColumnTypeName(column);
            Object columnValue = result.getObject(column);

            DBColumn<Object> dbColumn = new DBColumn<>();
//            dbColumn.setTableName(tableName);
            dbColumn.setColumnClassName(columnClassName);
            dbColumn.setColumnTypeName(columnTypeName);
            dbColumn.setColumnName(columnName);
            dbColumn.setColumnLabel(columnLabel);
            dbColumn.setColumnValue(columnValue);

            System.out.println(dbColumn);

            dataNameList.add(columnName);
            dataMap.put(columnName, dbColumn);
        }
        System.out.println();
    }

    @Override
    public void write(PreparedStatement stmt) throws SQLException {
//        System.out.println("public void write(PreparedStatement stmt) throws SQLException");
        if (dataNameList != null && dataMap != null) {
            int dataListSize = dataNameList.size();
            int dataMapSize = dataMap.size();
            if (dataListSize != dataMapSize) {
                throw new IllegalStateException("Data size wrong");
            }
            if (dataListSize > 0 && dataMapSize > 0) {
                for (int index = 0; index < dataListSize; index++) {
                    String columnName = dataNameList.get(index);
                    DBColumn<Object> dbColumn = dataMap.get(columnName);
                    stmt.setObject(index + 1, dbColumn.getColumnValue());
                }
            }
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
//        this.id = in.readLong();
//        this.name = Text.readString(in);
        System.out.println("public void readFields(DataInput dataInput) throws IOException");
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
//        out.writeLong(this.id);
//        Text.writeString(out, this.name);
        System.out.println("public void write(DataOutput dataOutput) throws IOException");
    }

    public Object get(String columnName) {
        if (dataNameList == null || dataMap == null) {
            return null;
        }
        int dataListSize = dataNameList.size();
        int dataMapSize = dataMap.size();
        if (dataListSize < 1 || dataMapSize < 1) {
            return null;
        }
        if (dataListSize != dataMapSize) {
            throw new IllegalStateException("Data size wrong");
        }
        return dataMap.get(columnName).getColumnValue();
    }

    public void set(String columnName, Object columnValue, boolean clear) {
        if (clear) {
            dataNameList.clear();
            dataMap.clear();
        }
        DBColumn<Object> dbColumn = new DBColumn<>();
        dbColumn.setColumnName(columnName);
        dbColumn.setColumnLabel(columnName);
        dbColumn.setColumnValue(columnValue);

        dataNameList.add(columnName);
        dataMap.put(columnName, dbColumn);
    }
}
