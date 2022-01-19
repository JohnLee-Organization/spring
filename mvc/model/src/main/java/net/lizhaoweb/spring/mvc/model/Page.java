/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:20
 */
package net.lizhaoweb.spring.mvc.model;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>模型 [数据] - 分页</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Page implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = 5828211920131239865L;

    public final static int DEFAULT_PAGESIZE = 10;

    /**
     * 当前页码
     */
    @Getter
    private int pageNo;

    /**
     * 每页行数
     */
    @Getter
    private int pageSize;

    /**
     * 总记录数
     */
    @Getter
    private long totalRecord;

    /**
     * 总页数
     */
    @Getter
    private int totalPage;

    // private Map<String, String> params; // 查询条件
    // private Map<String, List<String>> paramLists; // 数组查询条件
    // private String searchUrl; // Url地址

    /**
     * 可以显示的页号(分隔符"|"，总页数变更时更新)
     */
    @Getter
    private String pageNoDisp;

    public Page() {
        this(1, DEFAULT_PAGESIZE);
    }

    /**
     * 分页构造
     *
     * @param pageNo 页码
     */
    public Page(int pageNo) {
        this(pageNo, DEFAULT_PAGESIZE);
    }

    /**
     * 分页构造
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     */
    public Page(int pageNo, int pageSize) {
        super();
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        this.setTotalRecord(0);
        // params = new HashMap<String, String>();
        // paramLists = new HashMap<String, List<String>>();
        // searchUrl = "";
    }

    public void setPageNo(int pageNo) {
        if (pageNo < 1 || this.totalPage < 1) {
            pageNo = 1;
        } else if (pageNo > this.totalPage) {
            pageNo = this.totalPage;
        }
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGESIZE;
        }
        this.pageSize = pageSize;
    }

    public void setTotalRecord(long totalCount) {
        if (totalCount < 0) {
            totalCount = 0;
        }
        this.totalRecord = totalCount;
        refreshPage();
    }

    /**
     * 总件数变化时，更新总页数并计算显示样式
     */
    private void refreshPage() {
        // 总页数计算
        this.totalPage = (int) (this.totalRecord % this.pageSize == 0 ? this.totalRecord / this.pageSize : (this.totalRecord / this.pageSize + 1));
        // 防止超出最末页（浏览途中数据被删除的情况）
        if (this.pageNo > this.totalPage && this.totalPage != 0) {
            this.pageNo = this.totalPage;
        }
        pageNoDisp = computeDisplayStyleAndPage();
    }

    /**
     * 计算页号显示样式 这里实现以下的分页样式("[]"代表当前页号)，可根据项目需求调整 [1],2,3,4,5,6,7,8..12,13
     * 1,2..5,6,[7],8,9..12,13 1,2..6,7,8,9,10,11,12,[13]
     */
    private String computeDisplayStyleAndPage() {
        List<Integer> pageDisplays = new ArrayList<Integer>();
        if (totalPage <= 11) {
            for (int i = 1; i <= totalPage; i++) {
                pageDisplays.add(i);
            }
        } else if (pageNo < 7) {
            for (int i = 1; i <= 8; i++) {
                pageDisplays.add(i);
            }
            pageDisplays.add(0);// 0 表示 省略部分(下同)
            pageDisplays.add(totalPage - 1);
            pageDisplays.add(totalPage);
        } else if (pageNo > totalPage - 6) {
            pageDisplays.add(1);
            pageDisplays.add(2);
            pageDisplays.add(0);
            for (int i = totalPage - 7; i <= totalPage; i++) {
                pageDisplays.add(i);
            }
        } else {
            pageDisplays.add(1);
            pageDisplays.add(2);
            pageDisplays.add(0);
            for (int i = pageNo - 2; i <= pageNo + 2; i++) {
                pageDisplays.add(i);
            }
            pageDisplays.add(0);
            pageDisplays.add(totalPage - 1);
            pageDisplays.add(totalPage);
        }
        return Joiner.on("|").join(pageDisplays.toArray());
    }
}
