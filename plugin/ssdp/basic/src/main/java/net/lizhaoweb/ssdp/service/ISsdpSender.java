/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service
 * @date : 2024-03-06
 * @time : 13:39
 */
package net.lizhaoweb.ssdp.service;

import java.net.InetAddress;

/**
 * [业务层] [接口] 发送器
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:39
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
public interface ISsdpSender<REQ> {

    /**
     * 发送。
     *
     * @param inetAddress 目标地址
     * @param port        目标端口
     * @param message     消息
     * @return boolean
     */
    boolean send(InetAddress inetAddress, int port, REQ message);
}
