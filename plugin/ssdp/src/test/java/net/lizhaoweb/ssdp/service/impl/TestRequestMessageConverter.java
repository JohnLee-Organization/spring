/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 01:53
 */
package net.lizhaoweb.ssdp.service.impl;

import net.lizhaoweb.ssdp.dto.MSearchRequest;
import net.lizhaoweb.ssdp.dto.NotifyRequest;
import net.lizhaoweb.ssdp.dto.Request;
import net.lizhaoweb.ssdp.model.EnumNotificationType;
import net.lizhaoweb.ssdp.model.EnumSearchTarget;
import net.lizhaoweb.ssdp.model.EnumServiceType;
import net.lizhaoweb.ssdp.service.IMessageConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:/schema/spring/application.xml"
})
public class TestRequestMessageConverter {

    @Resource(name = "requestMessageConverter")
    private IMessageConverter messageConverter;

    @Autowired
    private NotifyRequest notifyRequest;

    @Autowired
    private MSearchRequest mSearchRequest;

    @Test
    public void ssdpNotify() {
        notifyRequest.setServiceType(EnumServiceType.ROOT_DEVICE);
        notifyRequest.setNotificationType(EnumNotificationType.ALIVE);
        notifyRequest.install();
        String message = this.messageConverter.convert(notifyRequest);
        System.out.println(message);
    }

    @Test
    public void ssdpMSearch() {
        mSearchRequest.setSearchTarget(EnumSearchTarget.ROOT_DEVICE);
        mSearchRequest.install();
        String message = this.messageConverter.convert(mSearchRequest);
        System.out.println(message);
    }

    @Test
    public void ssdpNotifyToBean() {
        String message = "NOTIFY * HTTP/1.1\r\nUSN: uuid:0be23266-41b5-4ab7-83f0-b375870ae573::upnp:rootdevice\r\nNTS: ssdp:alive\r\nHost: 239.255.255.250:1900\r\nNT: upnp:rootdevice\r\nLocation: http://192.168.1.104:8200/rootDesc.xml\r\nServer: amd64 Windows 7/6.1 UPnP/1.0 LiZhao-SSDP/V1.0.0.0.1\r\nCache-Control: max-age=1810\r\n\r\n";
        Request bean = this.messageConverter.convert(message);
        System.out.println(bean);
    }

    @Test
    public void ssdpMSearchToBean() {
        String message = "M-SEARCH * HTTP/1.1\r\nMan: \"ssdp:discover\"\r\nMX: 3\r\nST: upnp:rootdevice\r\nHost: 239.255.255.250:1900\r\n\r\n";
        Request bean = this.messageConverter.convert(message);
        System.out.println(bean);
    }
}
