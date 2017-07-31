/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:24
 */
package net.lizhaoweb.spring.wordsegmenter.config;

import java.util.List;

/**
 * <h1>接口 - 配置管理类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IConfiguration {

    /**
     * 返回useSmart标志位
     * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @return useSmart
     */
    boolean useSmart();

    /**
     * 设置useSmart标志位
     * useSmart =true ，分词器使用智能切分策略， =false则使用细粒度切分
     *
     * @param useSmart 是否使用智能
     */
    void setUseSmart(boolean useSmart);

    /**
     * 获取主词典路径
     *
     * @return String 主词典路径
     */
    String getMainDictionary();

    /**
     * 获取量词词典路径
     *
     * @return String 量词词典路径
     */
    String getQuantifierDicionary();

    /**
     * 获取扩展字典配置路径
     *
     * @return List&lt;String&gt; 相对类加载器的路径
     */
    List<String> getExtDictionarys();

    /**
     * 获取扩展停止词典配置路径
     *
     * @return List&lt;String&gt; 相对类加载器的路径
     */
    List<String> getExtStopWordDictionarys();
}
