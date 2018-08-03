/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.service
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:38
 */
package net.lizhaoweb.ssdp.service;

import net.lizhaoweb.ssdp.dto.AbstractMessage;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * <h1>业务层 [接口] - 接收器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年12月02日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IReceiver<T extends AbstractMessage> extends Closeable, AutoCloseable {

    void init();

    /**
     * 接收。
     *
     * @return 消息包
     * @throws IOException 输入输出异常。
     */
    DatagramPacket receive() throws IOException;
}
