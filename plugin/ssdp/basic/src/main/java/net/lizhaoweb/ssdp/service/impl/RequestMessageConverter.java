/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service.impl
 * @date : 2024-03-08
 * @time : 15:29
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.config.SsdpConfiguration;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.model.dto.AbstractMessage;
import net.lizhaoweb.ssdp.model.dto.MSearchRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [业务层] [实现] - 请求消息转换器
 * <p>
 * Created by Jhon.Lee on 3/8/2024 15:29
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
public class RequestMessageConverter extends AbstractMessageConverter {

    private static Pattern pattern = Pattern.compile("^([^ ]+) +([^ ]+) +([^ /]+)/([^ /]+)$");

    /**
     * 有参构造。
     *
     * @param config SSDP 配置对象
     */
    public RequestMessageConverter(SsdpConfiguration config) {
        super(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    protected <T extends AbstractMessage> T buildBean(String message) {
        T bean = null;
        if (message.startsWith(SsdpMethod.M_SEARCH.getName())) {
            bean = (T) new MSearchRequest();
//        } else if (message.startsWith(SsdpMethod.NOTIFY.getName())) {
////            bean = (T) new NotifyRequest(config);
//        } else if (message.startsWith(SsdpMethod.GET.getName())) {
//            bean = (T) new MSearchRequest(config);
        }
        return bean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(StringBuilder builder, AbstractMessage message) {
        if (!(message instanceof SsdpRequest)) {
            throw new IllegalArgumentException(String.format("The '%s' is not a request message object", message));
        }
        SsdpRequest request = (SsdpRequest) message;
        builder.append(request.getMethod().getName()).append(" ").append(request.getQueryString()).append(" ").append(request.getTransportProtocol().getProtocol()).append("/").append(request.getTransportProtocol().getVersion()).append(Constant.Message.EOF.LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(AbstractMessage bean, String headerLine) {
        SsdpRequest request = (SsdpRequest) bean;
        Matcher matcher = pattern.matcher(headerLine);
        if (matcher.find()) {
            String methodString = matcher.group(1);
            String queryString = matcher.group(2);
            String transportProtocolName = matcher.group(3);
            String transportProtocolVersion = matcher.group(4);

            SsdpMethod method = SsdpMethod.fromName(methodString);
            SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            request.setMethod(method);
            request.setQueryString(queryString);
            request.setTransportProtocol(transportProtocol);
        }
    }
}
