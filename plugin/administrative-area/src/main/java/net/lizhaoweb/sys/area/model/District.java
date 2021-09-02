/*
 * Copyright (c) 2021, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.sys.area.model
 * @date : 2021-08-31
 * @time : 13:56
 */
package net.lizhaoweb.sys.area.model;

import lombok.Data;

import java.util.Date;

/**
 * [模型] - 行政区划
 * <p>
 * Created by Admin on 2021/8/31 13:56
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.1
 * @email 404644381@qq.com
 */
@Data
public class District {

    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 英文名称
     */
    private String englishName;

    /**
     * 拼音
     */
    private String pinYin;

    /**
     * 简拼
     */
    private String shortPinYin;

    /**
     * 类型。0是国，1是省，2是市，3是区、县
     */
    private int type;

    /**
     * 所处的层级
     */
    private int hierarchy;

    /**
     * 行政编码
     */
    private String districtCode;

    /**
     * 邮政编码
     */
    private String postCode;

    /**
     * 电话区号
     */
    private String areaCode;

    /**
     * WGS84坐标系的经度
     */
    private String lngWGS84;

    /**
     * WGS84坐标系的纬度
     */
    private String latWGS84;

    /**
     * GCJ02坐标系的经度
     */
    private String lngGCJ02;

    /**
     * GCJ02坐标系的纬度
     */
    private String latGCJ02;

    /**
     * DB09坐标系的经度
     */
    private String lngDB09;

    /**
     * DB09坐标系的纬度
     */
    private String latDB09;

    /**
     * 备注
     */
    private String memo;

    /**
     * 状态。0x00：删除；0x01：正常
     */
    private int state;

    /**
     * 数据统计日期
     */
    private Date statisticalDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 父标识
     */
    private Long parentId;

    /**
     * 父对象
     */
    private District parent;
}
