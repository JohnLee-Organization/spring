/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-13
 * @time : 17:35
 */
package net.lizhaoweb.ssdp.socket;

import net.lizhaoweb.ssdp.ISsdpClient;
import net.lizhaoweb.ssdp.config.SsdpConfiguration;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.service.ISsdpSender;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketCreateException;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketDataSendException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static net.lizhaoweb.ssdp.model._enum.SsdpMethod.M_SEARCH;

/**
 * []
 * <p>
 * Created by Jhon.Lee on 2024/3/13 17:35
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class SsdpSocketClient implements ISsdpClient, ISsdpSender<SsdpRequest>, ISsdpReceiver<SsdpResponse> {

    private SsdpConfiguration config;
    private InetAddress groupInetAddress;
    private int groupPort;

    public SsdpSocketClient(SsdpConfiguration config) {
        this.config = config;
        try {
            groupInetAddress = InetAddress.getByName(config.getBroadcastAddress());
        } catch (UnknownHostException e) {
            throw new SsdpUnknownHostException(e);
        }
        groupPort = config.getBroadcastPort();
    }

    @Override
    public SsdpResponse send(SsdpRequest request) {
        this.send(groupInetAddress, groupPort, request);
        if (M_SEARCH == request.getMethod()) {
            return this.receive();
        }
        return null;
    }

    // 发送消息
    @Override
    public boolean send(InetAddress inetAddress, int port, SsdpRequest message) {
        boolean result = false;
        RequestMessageConverter reqConverter = new RequestMessageConverter(config);
        byte[] responseMessage = reqConverter.toBytes(message);
        InetAddress groupInetAddress = null;
        try {
            groupInetAddress = InetAddress.getByName(config.getBroadcastAddress());
        } catch (Exception e) {
            throw new SsdpUnknownHostException(e);
        }
        int groupPort = config.getBroadcastPort();
        DatagramPacket datagramPacket = new DatagramPacket(responseMessage, responseMessage.length, groupInetAddress, groupPort);
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        try {
            socket.send(datagramPacket);
            result = true;
        } catch (Exception e) {
            throw new MulticastSocketDataSendException(e);
        } finally {
            while (!socket.isClosed()) {
                //TODO 超时处理
                socket.close();
//                try {
//                    sleep(1000);
//                } catch (Exception e) {
//                    // Nothing
//                }
            }
            socket = null;
        }
        return result;
    }

    // 接收消息
    @Override
    public SsdpResponse receive() {
        return null;
    }
}
