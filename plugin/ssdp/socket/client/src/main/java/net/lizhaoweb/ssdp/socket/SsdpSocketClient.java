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
import net.lizhaoweb.ssdp.exception.SsdpIOException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.service.ISsdpSender;
import net.lizhaoweb.ssdp.service.impl.RequestMessageConverter;
import net.lizhaoweb.ssdp.socket.config.ClientConfig;
import net.lizhaoweb.ssdp.socket.exception.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

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
public class SsdpSocketClient implements ISsdpClient, ISsdpSender<SsdpRequest>, ISsdpReceiver<SsdpRequest, SsdpResponse> {

    private ClientConfig config;

    private MulticastSocket multicastSocket;

    public SsdpSocketClient(ClientConfig config) {
        this.config = config;
        multicastSocket = this.buildMulticastSocket(config);
    }

    @Override
    public SsdpResponse send(SsdpRequest request) {
        this.send(config.getGroupInetAddress(), config.getGroupPort(), request);
        return this.receive(request);
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
    public SsdpResponse receive(SsdpRequest request) {
        DatagramPacket datagramPacket = new DatagramPacket(config.getPacketBuffer(), config.getPacketSize());
        try {
            multicastSocket.receive(datagramPacket);
        } catch (Exception e) {
            throw new MulticastSocketDataReceiveException(e);
        }
        String responseMessage = new String(datagramPacket.getData(), StandardCharsets.UTF_8);
        SsdpResponse response = config.getResponseMessageConverter().toBean(responseMessage);
        if (M_SEARCH == request.getMethod()) {
            return response;
        }
        return response;
    }

    public void close() {
        if (multicastSocket != null && multicastSocket.isClosed()) {
            multicastSocket.close();
        }
    }

    // 构建组播套节子
    private MulticastSocket buildMulticastSocket(ClientConfig config) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(config.getGroupPort());
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        try {
            socket.joinGroup(config.getGroupInetAddress());
        } catch (Exception e) {
            throw new MulticastSocketJoinGroupException(e);
        }
        try {
            socket.setTimeToLive(config.getTimeToLive());
            socket.setSoTimeout(config.getSoTimeout());
        } catch (SocketException e) {
            throw new MulticastSocketException(e);
        } catch (IOException e) {
            throw new SsdpIOException(e);
        }
        return socket;
    }

}
