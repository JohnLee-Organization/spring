/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-05-22
 * @time : 17:12
 */
package net.lizhaoweb.ssdp.socket.ipv4;

import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import net.lizhaoweb.ssdp.socket.ipv4.SsdpSocketServerForIpV4;

/**
 * [线程] SSDP服务器
 * <p>
 * Created by jhon on 2024/5/22 17:12
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class SsdpSocketServerThreadForIpV4 extends Thread {

    private SsdpSocketServerForIpV4 server;

    public SsdpSocketServerThreadForIpV4(ServerConfig config, IMessageFactory messageFactory) {
        server = new SsdpSocketServerForIpV4(config, messageFactory);
        server.init();
    }

    @Override
    public synchronized void start() {
        server.start();
        super.start();
    }

    @Override
    public void run() {
        server.run();
    }

    public void stopServer() {
        server.stop();
        server.close();
        server.destroy();
    }

    public boolean isDestroyed() {
        return server.isDestroyed();
    }
}
