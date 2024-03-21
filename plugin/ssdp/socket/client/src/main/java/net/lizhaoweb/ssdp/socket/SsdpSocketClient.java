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
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
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
        multicastSocket = this.buildMulticastSocket(config.getGroupInetAddress(), config.getGroupPort(), config.getTimeToLive(), config.getSoTimeout());
    }

    @Override
    public SsdpResponse send(SsdpRequest request) {
        SsdpResponse response = null;
        if (M_SEARCH == request.getMethod()) {
            try {
                response = this.receive(request);
                if (response != null) {
                    return response;
                }
            } catch (Throwable e) {
                // Nothing
            }
        }
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
        String responseMessage = null;
        try {
            if (M_SEARCH == request.getMethod()) {
                String maxWaitString = request.getHeader(SsdpHeaderType.MX);
                int maxWait = Integer.parseInt(maxWaitString) * 1000;
                this.socketSetSoTimeout(multicastSocket, maxWait);
            }
            do {
                multicastSocket.receive(datagramPacket);
                responseMessage = new String(datagramPacket.getData(), StandardCharsets.UTF_8);
            } while (responseMessage.startsWith("M-SEARCH"));
        } catch (Exception e) {
            throw new MulticastSocketDataReceiveException(e);
        } finally {
            if (M_SEARCH == request.getMethod()) {
                this.socketSetSoTimeout(multicastSocket, config.getSoTimeout());
            }
        }
        System.out.println("responseMessage=" + responseMessage);
        return config.getResponseMessageConverter().toBean(responseMessage);
    }

    public void close() {
        if (multicastSocket != null && multicastSocket.isClosed()) {
            multicastSocket.close();
        }
    }

    /**
     * 构建组播套节子
     *
     * @param groupInetAddress 组播地址
     * @param groupPort        组播端口
     * @param timeToLive       设置此套节子发送的组播数据包的默认生存时间，以控制组播的范围。
     *                         ttl必须在0<=ttl<=255的范围内，否则将引发IllegalArgumentException。
     *                         以TTL为0发送的组播数据包不在网络上传输，而是可以在本地传递。
     * @param soTimeout        指定的超时时间（以毫秒为单位），来启用/禁用SO_TIMEOUT选项。
     *                         如果此选项设置为非零时，则套节子的receive()方法将被阻塞到此时间量，
     *                         超过此时间量，虽然套节子仍然有效，但会抛出java.net.SocketTimeoutException。
     *                         必须在进入阻止操作之前启用该选项才能生效。超时值必须大于0。超时为零被解释为无限超时。
     * @return 组播套节子
     */
    private MulticastSocket buildMulticastSocket(InetAddress groupInetAddress, int groupPort, int timeToLive, int soTimeout) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(groupPort);
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        try {
            socket.joinGroup(groupInetAddress);
        } catch (Exception e) {
            throw new MulticastSocketJoinGroupException(e);
        }
        this.socketSetTimeToLive(socket, timeToLive);
        this.socketSetSoTimeout(socket, soTimeout);
        return socket;
    }

    /**
     * 组播套节子设置数据包的生存时间
     *
     * @param socket     组播套节子
     * @param timeToLive 设置此套节子发送的组播数据包的默认生存时间，以控制组播的范围。
     *                   ttl必须在0<=ttl<=255的范围内，否则将引发IllegalArgumentException。
     *                   以TTL为0发送的组播数据包不在网络上传输，而是可以在本地传递。
     */
    private void socketSetTimeToLive(MulticastSocket socket, int timeToLive) {
        try {
            if (timeToLive >= 0 && timeToLive <= 255) {
                socket.setTimeToLive(timeToLive);
            }
        } catch (SocketException e) {
            throw new MulticastSocketException(e);
        } catch (IOException e) {
            throw new SsdpIOException(e);
        }
    }

    /**
     * 数据套节子设置超时时间
     *
     * @param socket    数据套节子
     * @param soTimeout 指定的超时时间（以毫秒为单位），来启用/禁用SO_TIMEOUT选项。
     *                  如果此选项设置为非零时，则套节子的receive()方法将被阻塞到此时间量，
     *                  超过此时间量，虽然套节子仍然有效，但会抛出java.net.SocketTimeoutException。
     *                  必须在进入阻止操作之前启用该选项才能生效。超时值必须大于0。超时为零被解释为无限超时。
     */
    private void socketSetSoTimeout(DatagramSocket socket, int soTimeout) {
        try {
            socket.setSoTimeout(soTimeout);
        } catch (SocketException e) {
            throw new MulticastSocketException(e);
        }
    }
}
