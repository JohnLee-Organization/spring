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

import net.lizhaoweb.ssdp.dto.MSearchResponse;
import net.lizhaoweb.ssdp.dto.Response;
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
public class TestResponseMessageConverter {

    @Resource(name = "responseMessageConverter")
    private IMessageConverter messageConverter;

    @Autowired
    private MSearchResponse mSearchResponse;

    @Test
    public void ssdpMSearch() {
        mSearchResponse.install();
        String message = this.messageConverter.convert(mSearchResponse);
        System.out.println(message);
    }

    @Test
    public void ssdpMSearchToBean() {
        String message = "HTTP/1.1 200 OK\r\nUSN: uuid:2a149bc1-090d-476f-a07c-e009ecb1d6d1::urn:schemas-upnp-org:device:MediaServer:1\r\nST: urn:schemas-upnp-org:device:MediaServer:1\r\nDate: Thu, 01 Dec 2016 13:51:09 GMT\r\nContent-Length: 0\r\nLocation: http://192.168.1.104:8200/rootDesc.xml\r\nEXT: \r\nServer: amd64 Windows 7/6.1 UPnP/1.0 LiZhao-SSDP/V1.0.0.0.1\r\nCache-Control: max-age=1810\r\n\r\n";
        Response bean = this.messageConverter.convert(message);
        System.out.println(bean);
    }
}
