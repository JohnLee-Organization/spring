/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.argument.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:32
 */
package net.lizhaoweb.spring.hadoop.commons.argument.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>枚举 - 选项</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum JLOption {

    JobName("jn", "jobName", "jobName", false, "The name of job", null),// 任务名称
    InputFromHDFS("ifhd", "hdfsIn", "inputFromHDFS", false, "The read path in HDFS", null),// 输入的 HDFS 路径
    OutputToHDFS("othd", "hdfsOut", "outputToHDFS", false, "The write path in HDFS", null),// 输出的 HDFS 路径

    ClusterMasterForHDFS("hdfsfmc", "hdfsCluster", "clusterMasterForHDFS", false, "The master-address of cluster for HDFS", null),// HDFS 集群地址
    InputRegexForMapReduce("irfmr", "inMapReduceRegex", "inputRegexForMapReduce", false, "The regex for map-reduce", null),// 输入 MapReduce 时的正则匹配
    InputRegexForMapper("irfm", "inMapperRegex", "inputRegexForMapper", false, "The regex for mapper", null),// 输入 Mapper 时的正则匹配
    InputRegexForReducer("irfr", "inReducerRegex", "inputRegexForReducer", false, "The regex for reducer", null),// 输入 Reduce 时的正则匹配

    RootPathForHBase("rpfhb", "hBaseRootDir", "rootPathForHBase", false, "The path of root for hBase", null),// HBase 集群地址
    ZooKeeperForHBase("zkfhb", "hBaseZookeeper", "zooKeeperForHBase", false, "The address of zoo-keeper for hBase", null),// HBase 集群地址
    ClusterMasterForHBase("cmfhb", "hBaseCluster", "clusterMasterForHBase", false, "The master-address of cluster for hBase", null),// HBase 集群地址
    TableNameForHBase("tnfhb", "hBaseTable", "tableNameForHBase", false, "The name of table for hBase", null),// HBASE 表名

    ClusterMasterForHive("cmfhi", "hiveCluster", "clusterMasterForHive", false, "The master-address of cluster for hive", null),// Hive 集群地址

    ;


    @Getter
    private String opt;

    @Getter
    private String longOpt;

    @Getter
    private String name;

    @Getter
    private boolean hasArg;

    @Getter
    private String description;

    @Getter
    private String defaultValue;
}
