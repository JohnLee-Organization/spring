/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service.impl
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 01:22
 */
package net.lizhaoweb.ssdp.service.impl;

import lombok.NoArgsConstructor;
import net.lizhaoweb.ssdp.dto.AbstractMessage;
import net.lizhaoweb.ssdp.dto.Response;
import net.lizhaoweb.ssdp.model.Configuration;
import net.lizhaoweb.ssdp.model.EnumTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>业务层 [实现] - 响应消息转换器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月01日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
public class ResponseMessageConverter extends AbstractMessageConverter {

    /**
     * 有参构造。
     *
     * @param configuration SSDP 配置对象
     */
    public ResponseMessageConverter(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected <T extends AbstractMessage> T buildBean(String message) {
        return (T) new Response();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void convertTop(StringBuilder builder, AbstractMessage message) {
        if (!(message instanceof Response)) {
            throw new IllegalArgumentException(String.format("The '%s' is not a response message object", message));
        }
        Response response = (Response) message;
        builder.append(response.getTransportProtocol().getProtocol()).append("/").append(response.getTransportProtocol().getVersion())
                .append(" ").append(response.getStatusCode()).append(" ").append(response.getMessage()).append(Constant.Message.EOF.LINE);
    }

    @Override
    protected void convertTop(AbstractMessage bean, String headerLine) {
        Response response = (Response) bean;
        Pattern pattern = Pattern.compile("^([^ /]+)/([^ /]+) +(\\d+) +([^ ]+)$");
        Matcher matcher = pattern.matcher(headerLine);
        if (matcher.find()) {
            String transportProtocolName = matcher.group(1);
            String transportProtocolVersion = matcher.group(2);
            String statusCodeString = matcher.group(3);
            String message = matcher.group(4);

            EnumTransportProtocol transportProtocol = EnumTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            response.setStatusCode(Integer.valueOf(statusCodeString));
            response.setMessage(message);
            response.setTransportProtocol(transportProtocol);
        }
    }
}
