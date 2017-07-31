/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 09:11
 */
package net.lizhaoweb.spring.mvc.uas.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <h1>模型 [数据] - 权限</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Authorization extends UASAbstractEntity<String> {

    private String id;

    /**
     * 路径
     */
    private String url;

    /**
     * 路径类型。0x00：项目内部(HTTP)。0x10：项目外部(HTTP)
     */
    private int urlType;

    /**
     * 角色列表。
     */
    private List<Role> roleList;
}
