/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.dao.write
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:47
 */
package net.lizhaoweb.spring.mvc.uas.dao.write;

import java.io.Serializable;

/**
 * <h1>持久层 [接口] 写 - SQL 操作</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface ISqlWriteMapper<ID extends Serializable> {

    /**
     * 创建数据表。
     */
    void createTable();

    /**
     * 更新记录。
     *
     * @param id 主键
     * @return 条数
     */
    int delete(ID id);

    /**
     * 更新记录。
     *
     * @param ids 主键列表
     * @return 条数
     */
    int remove(String ids);
}
