/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-20
 * @time : 17:31
 */
package net.lizhaoweb.ssdp.socket;

import net.lizhaoweb.ssdp.model.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.socket.config.ClientConfig;
import net.lizhaoweb.ssdp.socket.ipv4.SsdpSocketClientForIpV4;
import net.lizhaoweb.ssdp.socket.ipv6.SsdpSocketClientForIpV6;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static net.lizhaoweb.ssdp.model._enum.IpVersion.IPV4;
import static net.lizhaoweb.ssdp.model._enum.IpVersion.IPV6;
import static net.lizhaoweb.ssdp.util.Constant.DEFAULT_BROADCAST_ADDRESS_IPV4;
import static net.lizhaoweb.ssdp.util.Constant.DEFAULT_BROADCAST_ADDRESS_IPV6;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 17:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class TestSsdpSocketClient {

    @BeforeClass
    public static void setUpBeforeClass() {
        // 设置系统属性，指定logback配置文件位置
        System.setProperty("logback.configurationFile", "classpath:logback.xml");
    }

    @Test
    public void aaa() throws IOException {
        InetAddress groupInetAddress = InetAddress.getByName(DEFAULT_BROADCAST_ADDRESS_IPV4);
        int groupPort = 1900;
        MulticastSocket socket = new MulticastSocket(groupPort);
        socket.joinGroup(groupInetAddress);
        System.out.println(socket.getTimeToLive());
        System.out.println(socket.getSoTimeout());
        socket.leaveGroup(groupInetAddress);
        socket.close();
    }

    @Test
    public void testClientIpV4() {
        ClientConfig config = new ClientConfig(IPV4);
        config.setBroadcastAddressIpV4(DEFAULT_BROADCAST_ADDRESS_IPV4);
        config.setBroadcastPort(1900);
        config.setTimeToLive(255);
        SsdpSocketClientForIpV4 client = new SsdpSocketClientForIpV4(config);

        MSearchRequest mSearchRequest = new MSearchRequest();
        SsdpResponse response = client.send(mSearchRequest);
        System.out.println(response);

        client.close();
    }

    @Test
    public void testClientIpV6() {
        ClientConfig config = new ClientConfig(IPV6);
        config.setBroadcastAddressIpV4(DEFAULT_BROADCAST_ADDRESS_IPV6);
        config.setBroadcastPort(1900);
        config.setTimeToLive(255);
        SsdpSocketClientForIpV6 client = new SsdpSocketClientForIpV6(config);

        MSearchRequest mSearchRequest = new MSearchRequest();
        SsdpResponse response = client.send(mSearchRequest);
        System.out.println(response);

        client.close();
    }

    @Test
    public void ccc() {
        Date now = new Date();
        Locale locale = new Locale("ch.en");
        String dateUtcString = DateFormatUtils.formatUTC(now, "EEE, dd MMM yyyy HH:mm:ss zzz", locale);
        System.out.println(dateUtcString);
        String dateString = DateFormatUtils.format(now, "EEE, dd MMM yyyy HH:mm:ss z", TimeZone.getTimeZone("0"), locale);
        System.out.println(dateString);
        String dateString2 = DateFormatUtils.format(now, "EEE, dd MMM yyyy HH:mm:ss zzz", TimeZone.getTimeZone("0"));
        System.out.println(dateString2);
        System.out.println(now);
    }
}
