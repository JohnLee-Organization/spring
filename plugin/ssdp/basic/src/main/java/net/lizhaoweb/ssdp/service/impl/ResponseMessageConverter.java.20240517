/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service.impl
 * @date : 2024-03-08
 * @time : 17:29
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.config.SsdpConfiguration;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.model.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [业务层] [实现] - 响应消息转换器
 * <p>
 * Created by Jhon.Lee on 3/8/2024 17:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class ResponseMessageConverter extends AbstractMessageConverter {

    private static Pattern pattern = Pattern.compile("^([^ /]+)/([^ /]+) +(\\d+) +([^ ]+)$");

    /**
     * 有参构造。
     *
     * @param config SSDP 配置对象
     */
    public ResponseMessageConverter(SsdpConfiguration config) {
        super(config);
    }

    @Override
    protected <T extends AbstractMessage> T buildBean(String message) {
        return (T) new SsdpResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(StringBuilder builder, AbstractMessage message) {
        if (!(message instanceof SsdpResponse)) {
            throw new IllegalArgumentException(String.format("The '%s' is not a response message object", message));
        }
        SsdpResponse response = (SsdpResponse) message;
        builder.append(response.getTransportProtocol().getProtocol()).append("/").append(response.getTransportProtocol().getVersion())
                .append(" ").append(response.getCode()).append(" ").append(response.getCodeMessage()).append(Constant.Message.EOF.LINE);
    }

    @Override
    protected void convertTop(AbstractMessage bean, String headerLine) {
        SsdpResponse response = (SsdpResponse) bean;
        Matcher matcher = pattern.matcher(headerLine);
        if (matcher.find()) {
            String transportProtocolName = matcher.group(1);
            String transportProtocolVersion = matcher.group(2);
            String statusCodeString = matcher.group(3);
            String message = matcher.group(4);

            SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            response.setCode(Integer.parseInt(statusCodeString));
            response.setCodeMessage(message);
            response.setTransportProtocol(transportProtocol);
        }
    }
}
