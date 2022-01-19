/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 11:17
 */
package net.lizhaoweb.spring.mvc.uas.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <h1>模型 [数据] - 角色</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends UASAbstractEntity<Long> {

    private Long id;

    /**
     * 权限列表。
     */
    private List<Authorization> authorizationList;
}
