/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : hadoop
 * @Package : net.lizhaoweb.spring.hadoop.commons.argument.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:59
 */
package net.lizhaoweb.spring.hadoop.commons.argument.model;

import net.lizhaoweb.common.util.argument.model.AbstractArgument;

/**
 * <h1>枚举 - 参数</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年06月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class Argument extends AbstractArgument {

    public static final Argument JDBCDriver = new Argument("jdbcDriver", null, null);// JDBC 驱动
    public static final Argument JDBCUrl = new Argument("jdbcUrl", null, null);// JDBC 连接路径
    public static final Argument JDBCUsername = new Argument("jdbcUsername", null, null);// JDBC 用户名
    public static final Argument JDBCPassword = new Argument("jdbcPassword", null, null);// JDBC 密码
    public static final Argument JDBCSql = new Argument("jdbcSql", null, null);// JDBC SQL 语句

    public static final Argument JobName = new Argument("jobName", null, null);// 任务名称
    public static final Argument InputPathReg = new Argument("hdfsInReg", null, null);// 输入的 HDFS 路径
    public static final Argument InputPath = new Argument("hdfsIn", null, null);// 输入的 HDFS 路径
    public static final Argument OutputPath = new Argument("hdfsOut", null, null);// 输出的 HDFS 路径

    public static final Argument HDFSCluster = new Argument("hdfsCluster", null, null);// HDFS 集群地址
    public static final Argument MapperInputFormatRegex = new Argument("inMapperRegex", null, null);// 输入 Mapper 时的正则匹配
    public static final Argument ReduceInputFormatRegex = new Argument("inReduceRegex", null, null);// 输入 Reduce 时的正则匹配

    public static final Argument HBaseRootDir = new Argument("hBaseRootDir", null, null);// HBase 集群地址
    public static final Argument HBaseZookeeper = new Argument("hBaseZookeeper", null, null);// HBase 集群地址
    public static final Argument HBaseCluster = new Argument("hBaseCluster", null, null);// HBase 集群地址
    public static final Argument HbaseTable = new Argument("hBaseTable", null, null);// HBASE 表名

    public static final Argument HiveCluster = new Argument("hiveCluster", null, null);// Hive 集群地址

    public static final Argument Workbook = new Argument("workbook", null, null);// Workbook
    public static final Argument Sheet = new Argument("sheet", null, null);// Sheet
    public static final Argument Title = new Argument("title", null, null);// Title
    public static final Argument OperationMode = new Argument("operationMode", null, null);// OperationMode

    public Argument(String name, String nullValue, String[] nullArray) {
        super(name, nullValue, nullArray);
    }
}
