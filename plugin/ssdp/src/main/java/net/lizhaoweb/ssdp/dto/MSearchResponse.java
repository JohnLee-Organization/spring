/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.dto
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 22:48
 */
package net.lizhaoweb.ssdp.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.lizhaoweb.common.util.base.date.DateUtil;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.model.EnumHeaderName;
import net.lizhaoweb.ssdp.model.EnumServiceType;
import net.lizhaoweb.ssdp.model.EnumTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Locale;

/**
 * <h1>传输模型 [实现] - SSDP 协议查询响应</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
@AllArgsConstructor
public class MSearchResponse extends Response {

    // SSDP 配置对象
    @Autowired
    private Configuration configuration;

    /**
     * 安装
     */
    public void install() {
        this.setTransportProtocol(EnumTransportProtocol.HTTP_1_1);
        try {
            this.setStatusCode(200);
            this.setMessage("OK");
            this.setHeader(EnumHeaderName.DATE, DateUtil.date2GMT(new Date(), Constant.Message.DateFormat.RESPONSE, Locale.ENGLISH))// 指定响应生成的时间
                    .setHeader(EnumHeaderName.S, String.format("uuid:%s", Constant.Message.OS.SYSTEM_UUID))
                    .setHeader(EnumHeaderName.EXT, "")// 向控制点确认MAN头域已经被设备理解
                    .setHeader(EnumHeaderName.CACHE_CONTROL, String.format("max-age=%d", configuration.getMaxAge()))// max-age指定通知消息存活时间，如果超过此时间间隔，控制点可以认为设备不存在
                    .setHeader(EnumHeaderName.ST, EnumServiceType.MEDIA_SERVER.getValue() + 1)// 内容和意义与查询请求的相应字段相同
                    .setHeader(EnumHeaderName.USN, this.getUSNValue())// 表示不同服务的统一服务名，它提供了一种标识出相同类型服务的能力。
                    .setHeader(EnumHeaderName.SERVER, this.getServerValue(configuration.getProductName(), configuration.getProductVersion()))// 包含操作系统名，版本，产品名和产品版本信息
                    .setHeader(EnumHeaderName.LOCATION, this.getLocationValue(configuration.getUnicastPort()))// 包含根设备描述得URL地址
                    .setHeader(EnumHeaderName.CONTENT_LENGTH, "0");
        } catch (Exception e) {
            e.printStackTrace();
            this.setStatusCode(500);
            this.setMessage("Error");
        }
    }

    private String getUSNValue() {
        return String.format("uuid:%s::urn:schemas-upnp-org:device:MediaServer:%d", Constant.Message.OS.SYSTEM_UUID, 1);
    }

    private String getServerValue(String productName, String productVersion) {
        return String.format("%s %s/%s UPnP/1.0 %s/%s", OS_ARCH, OS_NAME, OS_VERSION, productName, productVersion);
    }

    private String getLocationValue(int port) {
        return String.format("http://%s:%d/rootDesc.xml", configuration.getHostAddress(), port);
    }
}
