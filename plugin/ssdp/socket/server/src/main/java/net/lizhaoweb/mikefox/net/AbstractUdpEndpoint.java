/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 13:37
 */
package net.lizhaoweb.mikefox.net;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;

/**
 * a
 * <p>
 * Created by jhon on 2024/6/6 13:37
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public abstract class AbstractUdpEndpoint extends AbstractEndpoint {

    /**
     * the packet data.
     */
    @Setter
    @Getter
    protected byte[] dataBuffer;

    /**
     * the packet data offset.
     */
    @Setter
    @Getter
    protected int dataOffset;

    /**
     * the packet data length.
     */
    @Setter
    @Getter
    protected int dataLength;

    /**
     * the destination address.
     */
    @Setter
    @Getter
    protected InetAddress destAddress;

    /**
     * the destination port number.
     */
    @Setter
    @Getter
    protected int destPort;

}
