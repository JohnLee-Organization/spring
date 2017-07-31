/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : MethodConfig.java
 * @Package : net.lizhaoweb.spring.solr
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年5月6日
 * @Time : 下午4:39:08
 */
package net.lizhaoweb.spring.solr;

import lombok.Data;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <h1>配置 - 数据层方法</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年5月6日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class MethodConfig {

    /**
     * 查询数据的必要条件
     */
    private String prerequisite;

    /**
     * Solr 返回的字段列表
     */
    private List<String> fields;

    /**
     * Solr 排序列表
     */
    private LinkedHashMap<String, ORDER> sorts;
}
