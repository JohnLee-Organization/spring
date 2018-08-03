/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl.socket
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:57
 */
package net.lizhaoweb.ssdp.service.impl.socket;

import net.lizhaoweb.ssdp.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.model.EnumSearchTarget;
import net.lizhaoweb.ssdp.service.IMessageConverter;
import net.lizhaoweb.ssdp.service.ISender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月08日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 * <p/>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:/schema/spring/application.xml"
})
public class TestMulticastSocketSender {

    @Autowired
    private Configuration configuration;

    @Resource(name = "requestMessageConverter")
    private IMessageConverter messageConverter;

    @Autowired
    private ISender sender;

    @Autowired
    private MSearchRequest mSearchRequest;

    @Test
    public void sendMSearch() {
        mSearchRequest.setSearchTarget(EnumSearchTarget.ROOT_DEVICE);
        mSearchRequest.install();
//        mSearchRequest.setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_IP, configuration.getHostAddress());
//        mSearchRequest.setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_PORT_HTTP, "8001");
//        mSearchRequest.setHeader(EnumHeaderName.SAVOR_SMALL_PLATFORM_PORT_NETTY, "8009");
        String message = this.messageConverter.convert(mSearchRequest);

        try {
            while (true) {
                sender.send(new InetSocketAddress("238.255.255.252", 11900), message.getBytes());
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
