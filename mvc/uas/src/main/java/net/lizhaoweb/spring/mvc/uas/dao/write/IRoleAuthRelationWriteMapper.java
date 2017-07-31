/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.write
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 09:17
 */
package net.lizhaoweb.spring.mvc.uas.dao.write;

import net.lizhaoweb.spring.mvc.uas.model.RoleAuthRelation;

/**
 * <h1>持久层 [接口] 写 - 角色权限关系</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IRoleAuthRelationWriteMapper extends ISqlWriteMapper<Long> {

    /**
     * 插入记录。
     *
     * @param bean 记录
     * @return 主键
     */
    long insert(RoleAuthRelation bean);
}
