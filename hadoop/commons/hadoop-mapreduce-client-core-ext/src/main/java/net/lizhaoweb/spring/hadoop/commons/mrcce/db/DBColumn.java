/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.mrcce.db
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:13
 */
package net.lizhaoweb.spring.hadoop.commons.mrcce.db;

import lombok.Data;

/**
 * <h1>模型 - 数据库返回结果转换对象</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年06月26日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class DBColumn<T> {

//    // 所在数据表
//    private String tableName;

    // 列名
    private String columnName;

    // 列标签
    private String columnLabel;

    // 列值
    private T columnValue;

    // 列值对应的类型名
    private String columnClassName;

    // 列对应的数据库类型
    private String columnTypeName;
}
