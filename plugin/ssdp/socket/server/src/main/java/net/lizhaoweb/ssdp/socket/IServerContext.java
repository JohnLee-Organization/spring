/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-14
 * @time : 11:10
 */
package net.lizhaoweb.ssdp.socket;

import java.net.DatagramPacket;

/**
 * [接口] 服务器上下文
 * <p>
 * Created by Jhon.Lee on 2024/3/14 11:10
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public interface IServerContext extends IContext {

    IServerApplication getApplication();

    DatagramPacket getDatagramPacket();
}
