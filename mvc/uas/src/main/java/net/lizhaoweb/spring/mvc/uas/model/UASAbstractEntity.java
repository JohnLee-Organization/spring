/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:18
 */
package net.lizhaoweb.spring.mvc.uas.model;

import lombok.Data;
import net.lizhaoweb.spring.mvc.model.AbstractEntity;

import java.io.Serializable;

/**
 * <h1>模型 [数据] - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public abstract class UASAbstractEntity<ID extends Serializable> extends AbstractEntity<ID> {

    /**
     * 描述。
     */
    private String description;
}
