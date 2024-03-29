/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : hadoop
 * @Package : org.apache.hadoop.mapreduce.lib.db
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:45
 */
package net.lizhaoweb.spring.hadoop.commons.mrcce.db;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * A OutputFormat that sends the reduce output to a Hive table.
 * <p>
 * {@link HiveOutputFormat} accepts &lt;key,value&gt; pairs, where
 * key has a type extending HiveWritable. Returned {@link RecordWriter}
 * writes <b>only the key</b> to the database with a batch Hive query.
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年06月19日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class HiveOutputFormat<K extends DBWritable, V> extends DBOutputFormat<K, V> {

//    private static final Log LOG = LogFactory.getLog(HiveOutputFormat.class);
//    public void checkOutputSpecs(JobContext context) throws IOException, InterruptedException {}
//
//    public OutputCommitter getOutputCommitter(TaskAttemptContext context) throws IOException, InterruptedException {
//        return new FileOutputCommitter(FileOutputFormat.getOutputPath(context), context);
//    }
//
//    /**
//     * Constructs the query used as the prepared statement to insert data.
//     *
//     * @param table
//     *          the table to insert into
//     * @param fieldNames
//     *          the fields to insert into. If field names are unknown, supply an
//     *          array of nulls.
//     */
//    public String constructQuery(String table, String[] fieldNames) {
//        if(fieldNames == null) {
//            throw new IllegalArgumentException("Field names may not be null");
//        }
//
//        StringBuilder query = new StringBuilder();
//        query.append("INSERT INTO ").append(table);
//
//        if (fieldNames.length > 0 && fieldNames[0] != null) {
//            query.append(" (");
//            for (int i = 0; i < fieldNames.length; i++) {
//                query.append(fieldNames[i]);
//                if (i != fieldNames.length - 1) {
//                    query.append(",");
//                }
//            }
//            query.append(")");
//        }
//        query.append(" VALUES (");
//
//        for (int i = 0; i < fieldNames.length; i++) {
//            query.append("?");
//            if(i != fieldNames.length - 1) {
//                query.append(",");
//            }
//        }
//        query.append(");");
//
//        return query.toString();
//    }

    /**
     * {@inheritDoc}
     */
    public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context) throws IOException {
        DBConfiguration dbConf = new DBConfiguration(context.getConfiguration());
        String tableName = dbConf.getOutputTableName();
        String[] fieldNames = dbConf.getOutputFieldNames();

        if (fieldNames == null) {
            fieldNames = new String[dbConf.getOutputFieldCount()];
        }

        try {
            Connection connection = dbConf.getConnection();
            PreparedStatement statement = connection.prepareStatement(constructQuery(tableName, fieldNames));
            return new HiveDBRecordWriter(connection, statement);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    /**
     * Initializes the reduce-part of the job with
     * the appropriate output settings
     *
     * @param job        The job
     * @param tableName  The table to insert data into
     * @param fieldNames The field names in the table.
     */
    public static void setOutput(Job job, String tableName, String... fieldNames) throws IOException {
        if (fieldNames.length > 0 && fieldNames[0] != null) {
            DBConfiguration dbConf = setOutput(job, tableName);
            dbConf.setOutputFieldNames(fieldNames);
        } else {
            if (fieldNames.length > 0) {
                setOutput(job, tableName, fieldNames.length);
            } else {
                throw new IllegalArgumentException("Field names must be greater than 0");
            }
        }
    }

    /**
     * Initializes the reduce-part of the job
     * with the appropriate output settings
     *
     * @param job        The job
     * @param tableName  The table to insert data into
     * @param fieldCount the number of fields in the table.
     */
    public static void setOutput(Job job, String tableName, int fieldCount) throws IOException {
        DBConfiguration dbConf = setOutput(job, tableName);
        dbConf.setOutputFieldCount(fieldCount);
    }

    private static DBConfiguration setOutput(Job job, String tableName) throws IOException {
        job.setOutputFormatClass(HiveOutputFormat.class);
        job.setReduceSpeculativeExecution(false);
        DBConfiguration dbConf = new DBConfiguration(job.getConfiguration());
        dbConf.setOutputTableName(tableName);
        return dbConf;
    }
}
