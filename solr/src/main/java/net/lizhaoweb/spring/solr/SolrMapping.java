/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : SolrMapping.java
 * @Package : net.lizhaoweb.spring.solr
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年5月5日
 * @Time : 下午10:44:42
 */
package net.lizhaoweb.spring.solr;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <h1>配置 - Solr Mapping</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年5月5日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class SolrMapping {

    /**
     * 类名
     */
    private String className;

    /**
     * Solr 请求地址
     */
    private String solrPath;

    /**
     * 字段映射列表
     */
    private List<SolrField> solrFields;

    /**
     * 数据层方法配置列表
     */
    private Map<String, MethodConfig> methodConfig;
}
