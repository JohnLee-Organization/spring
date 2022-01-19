/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:10
 */
package net.lizhaoweb.spring.mvc.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <h1>模型 [数据] - 基础抽象类</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = -2355167514203553556L;

    /**
     * 唯一标识
     */
    private ID id;

    /**
     * 名称
     */
    @NotBlank(message = "{validate.common.name.notBlank}")
    @Length(min = 2, max = 60, message = "{validate.common.name.length}")
    private String name;
}
