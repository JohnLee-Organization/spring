/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.search
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:14
 */
package net.lizhaoweb.spring.mvc.uas.search;

import net.lizhaoweb.spring.mvc.search.AbstractSearch;

import java.io.Serializable;

/**
 * <h1>模型 [查询] - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public abstract class UASAbstractSearch<ID extends Serializable> extends AbstractSearch<ID> {

    /**
     * 描述。
     */
    private String description;
}
