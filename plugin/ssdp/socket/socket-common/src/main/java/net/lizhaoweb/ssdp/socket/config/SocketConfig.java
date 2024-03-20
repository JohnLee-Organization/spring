/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.config
 * @date : 2024-03-20
 * @time : 17:42
 */
package net.lizhaoweb.ssdp.socket.config;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.ssdp.config.SsdpConfiguration;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/3/20 17:42
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public class SocketConfig extends SsdpConfiguration {

    /**
     * Set the default time-to-live for multicast packets sent out
     * on this {@code MulticastSocket} in order to control the
     * scope of the multicasts.
     *
     * <P> The ttl <B>must</B> be in the range {@code  0 <= ttl <=
     * 255} or an {@code IllegalArgumentException} will be thrown.
     * Multicast packets sent with a TTL of {@code 0} are not transmitted
     * on the network but may be delivered locally.
     */
    @Setter
    @Getter
    private int timeToLive;

    /**
     * Enable/disable SO_TIMEOUT with the specified timeout, in
     * milliseconds. With this option set to a non-zero timeout,
     * a call to receive() for this DatagramSocket
     * will block for only this amount of time.  If the timeout expires,
     * a <B>java.net.SocketTimeoutException</B> is raised, though the
     * DatagramSocket is still valid.  The option <B>must</B> be enabled
     * prior to entering the blocking operation to have effect.  The
     * timeout must be {@code > 0}.
     * A timeout of zero is interpreted as an infinite timeout.
     */
    @Setter
    @Getter
    private int soTimeout;
}
