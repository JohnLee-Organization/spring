/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.service
 * @date : 2024-03-07
 * @time : 17:57
 */
package net.lizhaoweb.ssdp.socket.service;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.ISsdpSender;
import net.lizhaoweb.ssdp.socket.IServerContext;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketCreateException;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketDataSendException;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * [] 客户请求线程
 * <p>
 * Created by Jhon.Lee on 3/7/2024 17:57
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class HandlerThread extends Thread implements ISsdpSender<SsdpResponse> {

    private IServerContext context;


    public HandlerThread(IServerContext context) {
        this.context = context;
    }

//    public ClientThread(SsdpRequest request, SsdpResponse response) {
//        this.request = request;
//        this.response = response;
//    }

    @Override
    public void run() {
        try {
            if (this.context.getDatagramPacket().getData().length < 1) {
                return;
            }
            String message = new String(this.context.getDatagramPacket().getData());
            log.trace("Thread[{}/{}] [Message] {}", this.getId(), this.getName(), message);
            SsdpRequest request = this.context.getApplication().getRequestMessageConverter().toBean(message);
            List<IServiceHandler<IServerContext, SsdpRequest, SsdpResponse>> handlerList = this.context.getApplication().getHandlerList(request.getMethod());
            if (handlerList == null) {
                return;
            }
            for (IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> handler : handlerList) {
                if (handler == null) {
                    continue;
                }
                SsdpResponse response = null;
                try {
                    response = handler.handle(this.context, request);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                if (response == null) {
                    return;
                }
                try {
                    InetAddress groupInetAddress = this.context.getApplication().getGroupInetAddress();
                    int groupPort = this.context.getApplication().getGroupPort();
                    this.send(groupInetAddress, groupPort, response);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean send(InetAddress inetAddress, int port, SsdpResponse message) {
        boolean result = false;
        byte[] responseMessage = this.context.getApplication().getResponseMessageConverter().toBytes(message);
        DatagramPacket datagramPacket = new DatagramPacket(responseMessage, responseMessage.length, inetAddress, port);
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
}
