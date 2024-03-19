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

import net.lizhaoweb.ssdp.socket.config.ServerSsdpConfiguration;
import net.lizhaoweb.ssdp.socket.handler.MSearchHandler;
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
public class SsdpSocketServerTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        // 设置系统属性，指定logback配置文件位置
        System.setProperty("logback.configurationFile", "F:\\WorkSpace\\JhonLee\\Java\\spring\\plugin\\ssdp\\socket\\server\\src\\test\\resources\\logback.xml");
    }

    @Test
    public void testServer() {
        try {
            ServerSsdpConfiguration config = new ServerSsdpConfiguration();
            config.getHandlerList().add(new MSearchHandler());
            config.setBroadcastAddress("239.255.255.250");
            config.setBroadcastPort(1900);
            final SsdpSocketServer server = new SsdpSocketServer(config);
            server.init();
            Thread thread = new Thread(server) {
                @Override
                public synchronized void start() {
                    server.start();
                    super.start();
                }
            };
            thread.start();
            boolean isStop = false;
            Scanner scanner = new Scanner(System.in);
            while (!isStop) {
                System.out.println("请输入命令：");
                String input = scanner.nextLine();
                if ("stop".equalsIgnoreCase(input)) {
                    isStop = true;
                }
            }
            scanner.close();
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void systemInfo() {
        System.out.println("=========================== ENV ===========================");
        Map<String, String> envMap = System.getenv();
        for (Map.Entry<String, String> envEntry : envMap.entrySet()) {
            System.out.printf("%s\0\0\0=\0\0\0%s\n", envEntry.getKey(), envEntry.getValue());
        }
        System.out.println();
        System.out.println();

        System.out.println("=========================== Property ===========================");
        Properties properties = System.getProperties();
        for (String propertyName : properties.stringPropertyNames()) {
            String propertyValue = properties.getProperty(propertyName);
            System.out.printf("%s\0\0\0=\0\0\0%s\0\0%s\n", propertyName, propertyValue, System.getProperty(propertyName));
        }
    }


    @Test
    public void cpu() {
        System.out.println(SystemUtil.getCPUSerialNumber());
    }
}
