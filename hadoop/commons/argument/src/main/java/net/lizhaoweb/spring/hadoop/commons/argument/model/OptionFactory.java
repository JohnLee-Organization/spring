/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.argument.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 13:40
 */
package net.lizhaoweb.spring.hadoop.commons.argument.model;

import org.apache.commons.cli.Option;

/**
 * <h1>工厂 - 选项</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年07月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class OptionFactory {

    /**
     * 任务名称
     *
     * @return {@link Option}
     */
    public static Option JobNameOption() {
        Option option = new Option(
                JLOption.JobName.getOpt(),
                JLOption.JobName.getLongOpt(),
                true,
                JLOption.JobName.getDescription()
        );
        option.setArgName(JLOption.JobName.getName());
        return option;
    }

    /**
     * 输入的 HDFS 路径
     *
     * @return {@link Option}
     */
    public static Option InputFromHDFSOption() {
        Option option = new Option(
                JLOption.InputFromHDFS.getOpt(),
                JLOption.InputFromHDFS.getLongOpt(),
                true,
                JLOption.InputFromHDFS.getDescription()
        );
        option.setArgName(JLOption.InputFromHDFS.getName());
        return option;
    }

    public static Option OutputToHDFSOption() {
        Option option = new Option(
                JLOption.OutputToHDFS.getOpt(),
                JLOption.OutputToHDFS.getLongOpt(),
                true,
                JLOption.OutputToHDFS.getDescription()
        );
        option.setArgName(JLOption.OutputToHDFS.getName());
        return option;
    }

    public static Option ClusterMasterForHDFSOption() {
        Option option = new Option(
                JLOption.ClusterMasterForHDFS.getOpt(),
                JLOption.ClusterMasterForHDFS.getLongOpt(),
                true,
                JLOption.ClusterMasterForHDFS.getDescription()
        );
        option.setArgName(JLOption.ClusterMasterForHDFS.getName());
        return option;
    }

    public static Option InputRegexForMapReduceOption() {
        Option option = new Option(
                JLOption.InputRegexForMapReduce.getOpt(),
                JLOption.InputRegexForMapReduce.getLongOpt(),
                true,
                JLOption.InputRegexForMapReduce.getDescription()
        );
        option.setArgName(JLOption.InputRegexForMapReduce.getName());
        return option;
    }

    public static Option InputRegexForMapperOption() {
        Option option = new Option(
                JLOption.InputRegexForMapper.getOpt(),
                JLOption.InputRegexForMapper.getLongOpt(),
                true,
                JLOption.InputRegexForMapper.getDescription()
        );
        option.setArgName(JLOption.InputRegexForMapper.getName());
        return option;
    }

    public static Option InputRegexForReducerOption() {
        Option option = new Option(
                JLOption.InputRegexForReducer.getOpt(),
                JLOption.InputRegexForReducer.getLongOpt(),
                true,
                JLOption.InputRegexForReducer.getDescription()
        );
        option.setArgName(JLOption.InputRegexForReducer.getName());
        return option;
    }

    public static Option RootPathForHBaseOption() {
        Option option = new Option(
                JLOption.RootPathForHBase.getOpt(),
                JLOption.RootPathForHBase.getLongOpt(),
                true,
                JLOption.RootPathForHBase.getDescription()
        );
        option.setArgName(JLOption.RootPathForHBase.getName());
        return option;
    }

    public static Option ZooKeeperForHBaseOption() {
        Option option = new Option(
                JLOption.ZooKeeperForHBase.getOpt(),
                JLOption.ZooKeeperForHBase.getLongOpt(),
                true,
                JLOption.ZooKeeperForHBase.getDescription()
        );
        option.setArgName(JLOption.ZooKeeperForHBase.getName());
        return option;
    }

    public static Option ClusterMasterForHBaseOption() {
        Option option = new Option(
                JLOption.ClusterMasterForHBase.getOpt(),
                JLOption.ClusterMasterForHBase.getLongOpt(),
                true,
                JLOption.ClusterMasterForHBase.getDescription()
        );
        option.setArgName(JLOption.ClusterMasterForHBase.getName());
        return option;
    }

    public static Option TableNameForHBaseOption() {
        Option option = new Option(
                JLOption.TableNameForHBase.getOpt(),
                JLOption.TableNameForHBase.getLongOpt(),
                true,
                JLOption.TableNameForHBase.getDescription()
        );
        option.setArgName(JLOption.TableNameForHBase.getName());
        return option;
    }

    public static Option ClusterMasterForHiveOption() {
        Option option = new Option(
                JLOption.ClusterMasterForHive.getOpt(),
                JLOption.ClusterMasterForHive.getLongOpt(),
                true,
                JLOption.ClusterMasterForHive.getDescription()
        );
        option.setArgName(JLOption.ClusterMasterForHive.getName());
        return option;
    }
}
