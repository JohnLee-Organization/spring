/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.model.dto
 * @date : 2024-03-06
 * @time : 14:02
 */
package net.lizhaoweb.ssdp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.lizhaoweb.ssdp.model._enum.SsdpTransportProtocol;
import net.lizhaoweb.ssdp.util.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [传输模型] SSDP响应
 * <p>
 * Created by Jhon.Lee on 3/6/2024 14:02
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused"})
public class SsdpResponse extends AbstractMessage {

    private static Pattern MESSAGE_FIRST_LINE_PATTERN = Pattern.compile("^([^ /]+)/([^ /]+) +(\\d+) +([^ ]+)$");

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态码对应的消息
     */
    private String codeMessage;

    @Override
    protected void convertFirstLine(StringBuilder builder) {
        builder.append(this.getTransportProtocol().getProtocol()).append("/").append(this.getTransportProtocol().getVersion()).append(" ");
        builder.append(this.getCode()).append(" ");
        builder.append(this.getCodeMessage());
        builder.append(Constant.Message.EOF.LINE);
    }

    @Override
    protected void convertFirstLine(String headerLine) {
        Matcher matcher = MESSAGE_FIRST_LINE_PATTERN.matcher(headerLine);
        if (matcher.find()) {
            String transportProtocolName = matcher.group(1);
            String transportProtocolVersion = matcher.group(2);
            String statusCodeString = matcher.group(3);
            String message = matcher.group(4);

            SsdpTransportProtocol transportProtocol = SsdpTransportProtocol.fromProtocol(transportProtocolName, transportProtocolVersion);
            this.setCode(Integer.parseInt(statusCodeString));
            this.setCodeMessage(message);
            this.setTransportProtocol(transportProtocol);
        }
    }
}
