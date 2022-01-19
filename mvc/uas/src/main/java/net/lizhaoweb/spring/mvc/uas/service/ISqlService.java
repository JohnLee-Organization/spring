/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 18:43
 */
package net.lizhaoweb.spring.mvc.uas.service;

import net.lizhaoweb.spring.mvc.model.DataDeliveryWrapper;
import net.lizhaoweb.spring.mvc.model.PageResult;
import net.lizhaoweb.spring.mvc.uas.model.UASAbstractEntity;
import net.lizhaoweb.spring.mvc.uas.search.UASAbstractSearch;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>业务层 [接口] - SQL 操作</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface ISqlService<ID extends Serializable, Bean extends UASAbstractEntity, Search extends UASAbstractSearch> {

    /**
     * 创建表
     */
    void createTable();

    /**
     * 检查并创建表
     */
    void checkAndCreateTable();

    /**
     * 插入记录。
     *
     * @param bean 记录
     * @return DataDeliveryWrapper
     */
    DataDeliveryWrapper<Object> save(Bean bean);

    /**
     * 更新记录。
     *
     * @param bean 记录
     * @return DataDeliveryWrapper
     */
    DataDeliveryWrapper<Object> update(Bean bean);

    /**
     * 删除记录。
     *
     * @param id 主键
     * @return DataDeliveryWrapper
     */
    DataDeliveryWrapper<Object> delete(ID id);

    /**
     * 批量删除记录。
     *
     * @param ids 主键
     * @return DataDeliveryWrapper
     */
    DataDeliveryWrapper<Object> remove(String ids);

    /**
     * 根据查询条件查询所有记录。
     *
     * @param search 查询条件
     * @return DataDeliveryWrapper
     */
    DataDeliveryWrapper<PageResult<List<Bean>>> pageList(Search search);

//    /**
//     * 根据查询条件查询所有记录。
//     *
//     * @param search 查询条件
//     * @return 记录列表
//     */
//    List<Bean> findAll(Search search);

//    /**
//     * 权限查询条件查询记录。
//     *
//     * @param search 查询条件
//     * @return 记录
//     */
//    Bean find(Search search);

    /**
     * 权限主键查询记录。
     *
     * @param id 主键
     * @return 记录
     */
    Bean get(ID id);
}
