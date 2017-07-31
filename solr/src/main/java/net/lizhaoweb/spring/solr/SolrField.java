/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : SolrField.java
 * @Package : net.lizhaoweb.spring.solr
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年5月5日
 * @Time : 下午10:45:22
 */
package net.lizhaoweb.spring.solr;

import lombok.Data;

/**
 * <h1>配置 - 字段映射</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年5月5日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class SolrField {

    /**
     * Solr 字段名
     */
    private String solrName;

    /**
     * 类中属性名
     */
    private String beanName;
}
