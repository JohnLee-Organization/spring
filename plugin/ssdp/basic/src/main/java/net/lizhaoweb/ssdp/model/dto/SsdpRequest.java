/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 13:54
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lizhaoweb.ssdp.model._enum.SsdpMethod;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;

import java.io.InputStream;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [传输模型] SSDP请求
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:54
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused"})
public abstract class SsdpRequest extends AbstractMessage {

    protected static Pattern MESSAGE_FIRST_LINE_PATTERN = Pattern.compile("^([^ ]+) +([^ ]+) +([^ /]+)/([^ /]+)$");

    /**
     * 目标地址.
     */
    private SocketAddress targetSocketAddress;

    /**
     * SSDP 方法
     */
    private SsdpMethod method;

    /**
     * 请求路径
     */
    private String queryString;

    /**
     * 输入流
     */
    private InputStream inputStream;

    @Override
    protected void convertFirstLine(StringBuilder builder) {
        builder.append(this.getMethod().getName()).append(" ");
        builder.append(this.getQueryString()).append(" ");
        builder.append(this.getTransportProtocol().getProtocol()).append("/").append(this.getTransportProtocol().getVersion());
        builder.append(Constant.Message.EOF.LINE);
    }

    @Override
    protected void convertFirstLine(String headerLine) {
        Matcher matcher = MESSAGE_FIRST_LINE_PATTERN.matcher(headerLine);
        if (matcher.find()) {
            String methodString = matcher.group(1);
            String queryString = matcher.group(2);
            String transportProtocolName = matcher.group(3);
            String transportProtocolVersion = matcher.group(4);

            SsdpMethod method = SsdpMethod.fromName(methodString);
            SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            this.setMethod(method);
            this.setQueryString(queryString);
            this.setTransportProtocol(transportProtocol);
        }
    }
}
