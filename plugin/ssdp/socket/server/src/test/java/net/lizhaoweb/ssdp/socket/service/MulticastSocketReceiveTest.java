/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.service
 * @date : 2024-03-07
 * @time : 10:48
 */
package net.lizhaoweb.ssdp.socket.service;

import org.junit.Test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

/**
 * 测试MulticastSocket接收
 * <p>
 * Created by Jhon.Lee on 3/7/2024 10:48
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class MulticastSocketReceiveTest {

    @Test
    public void receive() {
        MulticastSocket socket = null;
        InetSocketAddress inetSocketAddress = new InetSocketAddress("239.255.255.250", 1900);
        try {
            System.out.println("------------------服务端（接收端）启动------------------");

            // 1、创建DatagramSocket对象（接收端对象）：必须指定端口————>接韭菜的人
            System.out.println("Port=" + inetSocketAddress.getPort());
            socket = new MulticastSocket(inetSocketAddress.getPort());

            // 把当前接收端加入到一个组播组中：绑定对应的组播消息的组播IP
            // jdk14开始淘汰了此API，但是在企业中还是可以使用的，因为企业中还用不到这么新的jdk版本
            System.out.println("InetSocketAddress=" + inetSocketAddress + "  InetAddress=" + inetSocketAddress.getAddress() + ",,,," + InetAddress.getByName("239.255.255.250"));
            socket.joinGroup(inetSocketAddress.getAddress());
//            socket.joinGroup(InetAddress.getByName("239.255.255.250"));
//        socket.joinGroup(new InetSocketAddress(InetAddress.getByName("224.0.1.1"), 9999),
//                NetworkInterface.getByInetAddress(InetAddress.getLocalHost())); // 新API，了解一下

            // 2、创建字节数组
            int cacheSize = 1024 * 64; // 64KB
            byte[] cacheBuffer = new byte[cacheSize];

            // 3、创建DatagramPacket对象（数据包对象）————>韭菜盘子
            DatagramPacket packet = new DatagramPacket(cacheBuffer, cacheSize);

            // 5、使用while死循环不断调用DatagramSocket对象的receive方法传入DatagramPacket对象————>接住抛过来的韭菜
            while (true) {
                socket.receive(packet);
                System.out.println("收到数据：" + packet.getSocketAddress() + "<<<>>>" + packet.getAddress() + "<<<>>>" + packet.getPort());

                // 读取多少倒出多少
                int len = packet.getLength();
                System.out.println("Receive >>=" + len + ", ip=" + packet.getAddress() + ", port=" + packet.getPort() + ", SocketAddress=" + packet.getSocketAddress());
                // 6、每接收到一条弹幕消息，展示出来
                String rs = new String(cacheBuffer, 0, len);
                System.out.println(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.leaveGroup(inetSocketAddress.getAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                socket.close();
            }
        }
    }
}
