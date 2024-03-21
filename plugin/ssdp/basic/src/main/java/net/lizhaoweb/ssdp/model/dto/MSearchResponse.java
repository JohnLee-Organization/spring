/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-08
 * @time : 15:04
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

import static net.lizhaoweb.ssdp.util.Constant.Message.*;
import static org.apache.commons.lang3.SystemUtils.*;

/**
 * [传输模型] SSDP查询响应
 * <p>
 * Created by Jhon.Lee on 3/8/2024 15:04
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class MSearchResponse extends SsdpResponse {

    public MSearchResponse() {
        this.setTransportProtocol(SsdpTransportProtocol.HTTP_1_1);
        try {
            this.setCode(200);
            this.setCodeMessage("OK");
            this.setHeader(SsdpHeaderType.DATE, DateFormatUtils.format(new Date(), DateFormat.RESPONSE, _TimeZone.RESPONSE, _Locale.RESPONSE)) // 指定响应生成的时间
//                    .setHeader(SsdpHeaderType.S, String.format("uuid:%s", Constant.Message.OS.SYSTEM_UUID)) //
                    .setHeader(SsdpHeaderType.EXT) // 向控制点确认MAN头域已经被设备理解
                    .setHeader(SsdpHeaderType.CACHE_CONTROL) // max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在。默认：1810
                    .setHeader(SsdpHeaderType.ST) // 内容和意义与查询请求的相应字段相同
                    .setHeader(SsdpHeaderType.USN, String.format("uuid:%s::%s", OS.SYSTEM_UUID, this.getHeader(SsdpHeaderType.ST))) // 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力。
                    .setHeader(SsdpHeaderType.SERVER) // 包含操作系统名，版本，产品名和产品版本信息
                    .setHeader(SsdpHeaderType.LOCATION) // 包含根设备描述得URL地址
//                    .setHeader(SsdpHeaderType.CONTENT_LENGTH, 0)
            ;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            this.setCode(500);
            this.setCodeMessage("Error");
        }
    }

    /**
     * 设置消息存活时间
     *
     * @param maxAge 最大消息存活时间
     * @return MSearchResponse
     */
    public MSearchResponse setCacheMaxAge(int maxAge) {
        return this.setHeader(SsdpHeaderType.CACHE_CONTROL, String.format("max-age=%d", maxAge));
    }

    /**
     * 设置响应生成的时间
     *
     * @param date 响应生成的时间
     * @return MSearchResponse
     */
    public MSearchResponse setDate(Date date) {
        return this.setHeader(SsdpHeaderType.DATE, DateFormatUtils.formatUTC(date, DateFormat.RESPONSE));
    }

    /**
     * 设置产品名和产品版本信息
     *
     * @param productName    产品名
     * @param productVersion 产品版本
     * @return MSearchResponse
     */
    public MSearchResponse setProductVersion(String productName, String productVersion) {
        return this.setHeader(SsdpHeaderType.SERVER, String.format("%s %s/%s UPnP/1.0 %s/%s", OS_ARCH, OS_NAME, OS_VERSION, productName, productVersion));
    }

    /**
     * 设置包含根设备描述得URL地址。
     * <p>
     * 例如：<a href=":;">http://192.168.1.20:8200/rootDesc.xml</a>
     *
     * @param rootDescXml 包含根设备描述得URL地址
     * @return MSearchResponse
     */
    public MSearchResponse setLocation(String rootDescXml) {
        return this.setHeader(SsdpHeaderType.LOCATION, rootDescXml);
    }
}
