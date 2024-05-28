/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.ipv4
 * @date : 2024-05-27
 * @time : 17:48
 */
package net.lizhaoweb.ssdp.socket.ipv4;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.ISsdpClient;
import net.lizhaoweb.ssdp.exception.SsdpIOException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model._enum.SsdpHeaderType;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.service.ISsdpSender;
import net.lizhaoweb.ssdp.service.impl.DefaultMessageFactory;
import net.lizhaoweb.ssdp.socket.AbstractSsdpSocketClient;
import net.lizhaoweb.ssdp.socket.config.ClientConfig;
import net.lizhaoweb.ssdp.socket.exception.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static net.lizhaoweb.ssdp.model._enum.SsdpMethod.M_SEARCH;

/**
 * a
 * <p>
 * Created by jhon on 2024/5/27 17:48
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
@Slf4j
public class SsdpSocketClientForIpV4 extends AbstractSsdpSocketClient {

    public SsdpSocketClientForIpV4(ClientConfig config) {
        super(config);
    }

    @Override
    protected InetAddress getBroadcastInetAddress(ClientConfig config) {
        InetAddress groupInetAddress = null;
        try {
            groupInetAddress = InetAddress.getByName(config.getBroadcastAddressIpV4());
        } catch (Exception e) {
            throw new SsdpUnknownHostException(e);
        }
        return groupInetAddress;
    }
}
