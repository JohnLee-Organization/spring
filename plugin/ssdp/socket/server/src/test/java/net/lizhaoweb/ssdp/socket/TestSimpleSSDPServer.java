/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-14
 * @time : 10:05
 */
package net.lizhaoweb.ssdp.socket;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/14 10:05
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class TestSimpleSSDPServer {

    public static void main(String[] args) {
        try {
            // 创建JmDNS对象
            JmDNS jmdns = JmDNS.create();

            // 创建ServiceInfo
            ServiceInfo serviceInfo = ServiceInfo.create("_device._tcp.local.", "UniqueDeviceName", 80, "path=/description.xml");

            // 注册服务
            jmdns.registerService(serviceInfo);

            System.out.println("SSDP Server is running...");

            // 防止程序退出
            Thread.currentThread().join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
