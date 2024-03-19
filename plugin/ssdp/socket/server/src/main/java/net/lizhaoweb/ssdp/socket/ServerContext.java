/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-11
 * @time : 11:20
 */
package net.lizhaoweb.ssdp.socket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.ssdp.socket.exception.SocketCreateException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * [服务器] 上下文
 * <p>
 * Created by Jhon.Lee on 3/11/2024 11:20
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
public class ServerContext implements IServerContext {

    /**
     * SSDP配置对象
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private ServerApplication application;


    /**
     * 数据包对象
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private DatagramPacket datagramPacket;

    /**
     * 发送套节子
     */
    @Setter(AccessLevel.NONE)
    @Getter
    private DatagramSocket socket;

    /**
     * [服务器] 运行上下文
     *
     * @param application 服务器应用
     */
    public ServerContext(ServerApplication application) {
        this.application = application;
        this.datagramPacket = new DatagramPacket(this.application.getPacketBuffer(), this.application.getPacketSize());
        try {
            this.socket = new DatagramSocket();
        } catch (Exception e) {
            throw new SocketCreateException(e);
        }
    }
}
