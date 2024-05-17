/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service.impl
 * @date : 2024-05-17
 * @time : 12:16
 */
package net.lizhaoweb.ssdp.service.impl;

import net.lizhaoweb.ssdp.exception.SsdpIllegalAccessException;
import net.lizhaoweb.ssdp.exception.SsdpInstantiationException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownMessageTypeException;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.model.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.IMessageFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * [实现] 消息工厂
 * <p>
 * Created by jhon on 2024/5/17 12:16
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class DefaultMessageFactory implements IMessageFactory {

//    private final static String REQUEST = "REQUEST";
//    private final static String RESPONSE = "RESPONSE";
//
//    private final static Map<String, Map<SsdpMethod, Class<?>>> MESSAGE_MAP = new HashMap<>();
//
//    static {
//        MESSAGE_MAP.put(REQUEST, new HashMap<>());
//        MESSAGE_MAP.put(RESPONSE, new HashMap<>());
//    }

    private final static Map<SsdpMethod, Class<?>> REQUEST_MAP = new HashMap<>();
    private final static Map<SsdpTransportProtocol, Class<?>> RESPONSE_MAP = new HashMap<>();

    @Override
    public <T extends SsdpRequest> T toRequest(String message) {
        int index = message.indexOf(" ");
        String methodStr = message.substring(0, index);
        SsdpMethod method = SsdpMethod.fromName(methodStr);
        Class<?> clazz = REQUEST_MAP.get(method);
        T messageBean = null;
        try {
            messageBean = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new SsdpInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new SsdpIllegalAccessException(e);
        }
        messageBean.turnToMessage(message);
        return messageBean;
    }

    @Override
    public <T extends SsdpResponse> T toResponse(String message) {
        int index = message.indexOf(" ");
        String transportProtocolStr = message.substring(0, index);
        SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.fromProtocolVersion(transportProtocolStr);
        Class<?> clazz = RESPONSE_MAP.get(transportProtocol);
        T messageBean = null;
        try {
            messageBean = (T) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new SsdpInstantiationException(e);
        } catch (IllegalAccessException e) {
            throw new SsdpIllegalAccessException(e);
        }
        messageBean.turnToMessage(message);
        return messageBean;
    }

    public <T extends AbstractMessage> void register(T message) {
        if (message instanceof SsdpRequest) {
            SsdpRequest request = (SsdpRequest) message;
//            MESSAGE_MAP.get(REQUEST).put(request.getMethod(), request.getClass());
            REQUEST_MAP.put(request.getMethod(), request.getClass());
        } else if (message instanceof SsdpResponse) {
            SsdpResponse response = (SsdpResponse) message;
//            MESSAGE_MAP.get(RESPONSE).put(response.getCodeMessage(), response.getClass());
            RESPONSE_MAP.put(response.getTransportProtocol(), response.getClass());
        } else {
            throw new SsdpUnknownMessageTypeException("The type of the message is unknown.");
        }
    }
}
