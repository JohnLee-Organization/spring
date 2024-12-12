/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 12:53
 */
package net.lizhaoweb.mikefox.net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by jhon on 2024/6/6 12:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
class DefaultDatagramPacketFactory extends DatagramPacketFactory {

    DefaultDatagramPacketFactory() {
        /* NOTHING */
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int length) {
        return new DatagramPacket(buf, length);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int offset, int length) {
        return new DatagramPacket(buf, offset, length);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int length, InetAddress address, int port) {
        return new DatagramPacket(buf, length, address, port);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int offset, int length, InetAddress address, int port) {
        return new DatagramPacket(buf, offset, length, address, port);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int length, SocketAddress address) {
        return new DatagramPacket(buf, length, address);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int offset, int length, SocketAddress address) {
        return new DatagramPacket(buf, offset, length, address);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int length, String hostname, int port) {
        SocketAddress address = new InetSocketAddress(hostname, port);
        return createDatagramPacket(buf, length, address);
    }

    @Override
    public DatagramPacket createDatagramPacket(byte[] buf, int offset, int length, String hostname, int port) {
        SocketAddress address = new InetSocketAddress(hostname, port);
        return createDatagramPacket(buf, offset, length, address);
    }

}
