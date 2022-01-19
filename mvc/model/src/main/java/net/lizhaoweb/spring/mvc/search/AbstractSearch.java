/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.search
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:14
 */
package net.lizhaoweb.spring.mvc.search;

import lombok.Data;
import net.lizhaoweb.spring.mvc.model.Page;

import java.io.Serializable;

/**
 * <h1>模型 [查询] - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public abstract class AbstractSearch<ID extends Serializable> implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 2423914127866456367L;

    /**
     * 请求流水号，用于日志查询
     */
    private String SerialNumber;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 唯一标识
     */
    private ID id;

    /**
     * 名称
     */
    private String name;
}
