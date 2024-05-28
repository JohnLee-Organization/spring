/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-08
 * @time : 13:00
 */
package net.lizhaoweb.ssdp.socket;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.dto.MSearchResponse;
import net.lizhaoweb.ssdp.model.dto.NotifyRequest;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.service.impl.DefaultMessageFactory;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import net.lizhaoweb.ssdp.socket.handler.MSearchHandler;
import net.lizhaoweb.ssdp.socket.ipv4.SsdpSocketServerThreadForIpV4;
import net.lizhaoweb.ssdp.socket.ipv6.SsdpSocketServerThreadForIpV6;
import net.lizhaoweb.ssdp.util.SystemUtil;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * 测试服务器
 * <p>
 * Created by Jhon.Lee on 3/8/2024 13:00
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class TestSsdpSocketServer {

    @BeforeClass
    public static void setUpBeforeClass() {
        // 设置系统属性，指定logback配置文件位置
        System.setProperty("logback.configurationFile", "classpath:logback.xml");
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
    }

    @Test
    public void startUp() {
        try {
            ServerConfig config = new ServerConfig();
            config.getHandlerList().add(new MSearchHandler());
            config.setBroadcastAddressIpV4("239.255.255.250");
            config.setBroadcastAddressIpV6("FF02::C");
            config.setBroadcastPort(1900);
            config.setLocalAddressIpV4("0.0.0.0");
            config.setLocalAddressIpV6("::");
            config.setLocalPort(1901);
            IMessageFactory messageFactory = new DefaultMessageFactory();
            messageFactory.register(new MSearchRequest());
            messageFactory.register(new MSearchResponse());
            messageFactory.register(new NotifyRequest());
            SsdpSocketServerThreadForIpV4 threadIpV4 = null;
            if (config.isSupportIpV4()) {
                threadIpV4 = new SsdpSocketServerThreadForIpV4(config, messageFactory);
                threadIpV4.start();
            }
            SsdpSocketServerThreadForIpV6 threadIpV6 = null;
            if (config.isSupportIpV6()) {
                threadIpV6 = new SsdpSocketServerThreadForIpV6(config, messageFactory);
                threadIpV6.start();
            }
            boolean stopServer = false;
            Scanner scanner = new Scanner(System.in);
            while (!stopServer) {
                System.out.println("请输入命令：");
                String input = scanner.nextLine();
                if ("stop".equalsIgnoreCase(input)) {
                    stopServer = true;
                }
            }
            scanner.close();
            if (threadIpV4 != null) {
                threadIpV4.stopServer();
            }
            if (threadIpV6 != null) {
                threadIpV6.stopServer();
            }
            while (true) {
                if (threadIpV4 != null && threadIpV6 != null && threadIpV4.isDestroyed() && threadIpV6.isDestroyed()) {
                    break;
                } else if (threadIpV4 != null && threadIpV6 == null && threadIpV4.isDestroyed()) {
                    break;
                } else if (threadIpV4 == null && threadIpV6 != null && threadIpV6.isDestroyed()) {
                    break;
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(500);
        }
    }

    @Test
    public void systemInfo() {
        log.info("=========================== ENV ===========================");
        Map<String, String> envMap = System.getenv();
        for (Map.Entry<String, String> envEntry : envMap.entrySet()) {
            System.out.printf("%s\0\0\0=\0\0\0%s\n", envEntry.getKey(), envEntry.getValue());
        }
        log.info("");
        log.info("");

        log.info("=========================== Property ===========================");
        Properties properties = System.getProperties();
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            System.out.printf("%s\0\0\0=\0\0\0%s\0\0%s\n", propertyName, propertyValue, System.getProperty(propertyName));
        }
    }


    @Test
    public void cpu() {
        log.info(SystemUtil.getCPUSerialNumber());
    }
}
