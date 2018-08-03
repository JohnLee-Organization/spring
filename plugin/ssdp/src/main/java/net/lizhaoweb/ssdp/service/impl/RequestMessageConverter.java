/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 01:20
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.NoArgsConstructor;
import net.lizhaoweb.ssdp.dto.AbstractMessage;
import net.lizhaoweb.ssdp.dto.Request;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.model.EnumMethod;
import net.lizhaoweb.ssdp.model.EnumTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>业务层 [实现] - 请求消息转换器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
public class RequestMessageConverter extends AbstractMessageConverter {

    /**
     * 有参构造。
     *
     * @param configuration SSDP 配置对象
     */
    public RequestMessageConverter(Configuration configuration) {
        super(configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T extends AbstractMessage> T buildBean(String message) {
//        T bean = null;
//        if (message.startsWith(EnumMethod.NOTIFY.getName())) {
//            bean = (T) new NotifyRequest(configuration);
//        } else if (message.startsWith(EnumMethod.M_SEARCH.getName())) {
//            bean = (T) new MSearchRequest(configuration);
//        } else if (message.startsWith(EnumMethod.GET.getName())) {
//            bean = (T) new MSearchRequest(configuration);
//        }
        return (T) new Request();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(StringBuilder builder, AbstractMessage message) {
        if (!(message instanceof Request)) {
            throw new IllegalArgumentException(String.format("The '%s' is not a request message object", message));
        }
        Request request = (Request) message;
        builder.append(request.getMethod().getName()).append(" ").append(request.getQueryString()).append(" ")
                .append(request.getTransportProtocol().getProtocol()).append("/").append(request.getTransportProtocol().getVersion())
                .append(Constant.Message.EOF.LINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(AbstractMessage bean, String headerLine) {
        Request request = (Request) bean;
        Pattern pattern = Pattern.compile("^([^ ]+) +([^ ]+) +([^ /]+)/([^ /]+)$");
        Matcher matcher = pattern.matcher(headerLine);
        if (matcher.find()) {
            String methodString = matcher.group(1);
            String queryString = matcher.group(2);
            String transportProtocolName = matcher.group(3);
            String transportProtocolVersion = matcher.group(4);

            EnumMethod method = EnumMethod.fromName(methodString);
            EnumTransportProtocol transportProtocol = EnumTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            request.setMethod(method);
            request.setQueryString(queryString);
            request.setTransportProtocol(transportProtocol);
        }
    }
}
