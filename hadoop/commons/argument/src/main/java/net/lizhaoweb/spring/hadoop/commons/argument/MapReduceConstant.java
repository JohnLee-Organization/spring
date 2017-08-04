/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : hadoop
 * @Package : net.lizhaoweb.spring.hadoop.commons.argument
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:05
 */
package net.lizhaoweb.spring.hadoop.commons.argument;

import net.lizhaoweb.common.util.argument.ArgumentFactory;
import net.lizhaoweb.spring.hadoop.commons.argument.model.Argument;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;

/**
 * <h1>常量 - MapReduce</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年05月24日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class MapReduceConstant {
    public static final String DATA_FORMAT_1 = "dd/MMM/yyyy:HH:mm:ss Z";
    public static final String DATA_FORMAT_2 = "yyyy-MM-dd HH:mm:ss Z";
    public static final char HIVE_CONTENT_SPLIT_CHAR = 0x0001;// HIVE 文件内容默认分割符

    public static class CommonVariables {

        /**
         * 解析参数并初始化 MapReduce
         *
         * @param configuration MapReduce 配置对象
         * @param args          参数列表
         */
        public static void initMapReduce(Configuration configuration, String[] args) {
            ArgumentFactory.analysisArgument(args);

            // 配置 HDFS 根路径
            String hdfsCluster = ArgumentFactory.getParameterValue(Argument.HDFSCluster);
            ArgumentFactory.printInputArgument(Argument.HDFSCluster, hdfsCluster, false);
            if (StringUtils.isNotBlank(hdfsCluster)) {
                configuration.set("fs.defaultFS", hdfsCluster);
            }

            // 配置 HBase 的根路径
            String hbaseRoot = ArgumentFactory.getParameterValue(Argument.HBaseRootDir);
            ArgumentFactory.printInputArgument(Argument.HBaseRootDir, hbaseRoot, false);
            if (StringUtils.isNotBlank(hbaseRoot)) {
                configuration.set("hbase.rootdir", hbaseRoot);
            }

            // 配置 ZooKeeper 地址
            String hbaseZoo = ArgumentFactory.getParameterValue(Argument.HBaseZookeeper);
            ArgumentFactory.printInputArgument(Argument.HBaseZookeeper, hbaseZoo, false);
            if (StringUtils.isNotBlank(hbaseZoo)) {
                configuration.set("hbase.zookeeper.quorum", hbaseZoo);
            }
        }
    }
}
