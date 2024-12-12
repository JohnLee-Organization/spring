/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 13:01
 */
package net.lizhaoweb.mikefox.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

/**
 * Default server socket factory. Doesn't do much except give us
 * plain old server sockets.
 * <p>
 * Created by jhon on 2024/6/6 13:01
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */

// Default implementation of server sockets.

//
// WARNING: Some of the APIs in this class are used by J2EE.
// Please talk to harishp@eng.sun.com before making any changes.
//
@SuppressWarnings("unused")
class DefaultMulticastSocketFactory extends MulticastSocketFactory {

    DefaultMulticastSocketFactory() {
        /* NOTHING */
    }

    @Override
    public MulticastSocket createSocket(int port) throws IOException {
        return new MulticastSocket(port);
    }

    @Override
    public MulticastSocket createSocket(SocketAddress bindAddr) throws IOException {
        return new MulticastSocket(bindAddr);
    }

    @Override
    public MulticastSocket createSocket(int port, InetAddress bindAddr) throws IOException, InstantiationException {
        SocketAddress _bindAddr = new InetSocketAddress(bindAddr, port);
        return createSocket(_bindAddr);
    }

    @Override
    public MulticastSocket createSocket(int port, String hostname) throws IOException, InstantiationException {
        SocketAddress bindAddr = new InetSocketAddress(hostname, port);
        return createSocket(bindAddr);
    }

}
