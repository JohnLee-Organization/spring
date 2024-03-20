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

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

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

    @Test
    public void aaa() throws IOException {
        InetAddress groupInetAddress = InetAddress.getByName("239.255.255.250");
        int groupPort = 1900;
        MulticastSocket socket = new MulticastSocket(groupPort);
        socket.joinGroup(groupInetAddress);
        System.out.println(socket.getTimeToLive());
        System.out.println(socket.getSoTimeout());
        socket.leaveGroup(groupInetAddress);
        socket.close();
    }
}
