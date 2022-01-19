/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:07
 */
package net.lizhaoweb.spring.mvc.model;

import lombok.Data;

/**
 * <h1>模型 [数据] - 带分页的结果</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class PageResult<T> extends Page {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 9134559777005050142L;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 结果数据
     */
    private T result;

    public PageResult(Page page) {
        this.setPage(page);
    }

    public void setPage(Page page) {
        this.page = page;
        if (page != null) {
            this.setPageNo(page.getPageNo());
            this.setPageSize(page.getPageSize());
            this.setTotalRecord(page.getTotalRecord());
        }
    }
}
