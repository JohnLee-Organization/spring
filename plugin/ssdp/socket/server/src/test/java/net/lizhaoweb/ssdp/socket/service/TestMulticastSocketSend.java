/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.service
 * @date : 2024-03-11
 * @time : 10:41
 */
package net.lizhaoweb.ssdp.socket.service;

import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

/**
 * 测试MulticastSocket发送
 * <p>
 * Created by Jhon.Lee on 3/11/2024 10:41
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class TestMulticastSocketSend {

    @Test
    public void send() {
        DatagramSocket socket = null;
        InetSocketAddress inetSocketAddress = new InetSocketAddress("239.255.255.250", 1900);
        try {
            System.out.println("------------------客户端（发送端）启动------------------");
            // 1、创建DatagramSocket对象（发送端对象）：不指定端口时，自带默认的端口————>抛韭菜的人
//        DatagramSocket socket = new DatagramSocket(5555);   // 指定发送端端口为5555
//        DatagramSocket socket = new DatagramSocket(6666);
            socket = new DatagramSocket(7777);

            // 2、创建键盘输入对象，用于发送弹幕
            Scanner sc = new Scanner(System.in);

            // 3、使用while死循环不断的接收用户的数据输入
            while (true) {
                System.out.println("请您输入一条弹幕：");
                String data = sc.nextLine();

                // 判断条件：用户一旦输入exit，则结束发送
                if (data.equals("exit")) {
                    System.out.println("您已离线！！");
                    // 4、用户输入exit，结束发送后，释放资源
                    socket.close();
                    return;
                }

                // 5、创建字节数组，用于存储用户输入的弹幕
                byte[] buffer = data.getBytes();

                // 6、用户输入的不是exit，将数据封装成DatagramPacket对象————>韭菜盘子
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                System.out.println("Send >>=" + packet.getLength() + ", ip=" + packet.getAddress() + ", port=" + packet.getPort() + ", SocketAddress=" + packet.getSocketAddress());

                // 7、使用DatagramSocket对象的send方法将数据包对象进行发送————>抛出韭菜
                socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }


    @Test
    public void send2() {
        MulticastSocket socket = null;
        InetSocketAddress inetSocketAddress = new InetSocketAddress("239.255.255.250", 1900);
        try {
            socket = new MulticastSocket(inetSocketAddress.getPort());
            socket.joinGroup(inetSocketAddress.getAddress());//加入到组播组

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("请您输入一条弹幕：");
                String message = sc.nextLine();

                // 判断条件：用户一旦输入exit，则结束发送
                if (message.equals("exit")) {
                    System.out.println("您已离线！！");
                    socket.leaveGroup(inetSocketAddress.getAddress());
                    // 4、用户输入exit，结束发送后，释放资源
                    socket.close();
                    return;
                }

                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetSocketAddress.getAddress(), inetSocketAddress.getPort());
                socket.send(packet);//发送组播数据报
                System.out.println("Send >>=" + packet.getLength() + ", ip=" + packet.getAddress() + ", port=" + packet.getPort() + ", SocketAddress=" + packet.getSocketAddress());
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
