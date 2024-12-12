/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 12:55
 */
package net.lizhaoweb.mikefox.net;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Properties that can be set in the &lt;Connector&gt; element
 * in server.xml. All properties are prefixed with &quot;socket.&quot;
 * and are currently only working for the Nio connector
 * <p>
 * Created by jhon on 2024/6/6 12:55
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class SocketProperties {

    /**
     * Enable/disable key cache, this bounded cache stores
     * KeyAttachment objects to reduce GC
     * Default is 500
     * -1 is unlimited
     * 0 is disabled
     */
    @Setter
    @Getter
    protected int keyCache = 500;

    /**
     * Enable/disable socket processor cache, this bounded cache stores
     * SocketProcessor objects to reduce GC
     * Default is 500
     * -1 is unlimited
     * 0 is disabled
     */
    @Setter
    @Getter
    protected int processorCache = 500;

    /**
     * Enable/disable poller event cache, this bounded cache stores
     * PollerEvent objects to reduce GC for the poller
     * Default is 500
     * -1 is unlimited
     * 0 is disabled
     * >0 the max number of objects to keep in cache.
     */
    @Setter
    @Getter
    protected int eventCache = 500;

    /**
     * Enable/disable direct buffers for the network buffers
     * Default value is enabled
     */
    @Setter
    @Getter
    protected boolean directBuffer = false;

    /**
     * Socket receive buffer size in bytes (SO_RCVBUF).
     * JVM default used if not set.
     */
    @Setter
    @Getter
    protected Integer rxBufSize = null;

    /**
     * Socket send buffer size in bytes (SO_SNDBUF).
     * JVM default used if not set.
     */
    @Setter
    @Getter
    protected Integer txBufSize = null;

    /**
     * The application read buffer size in bytes.
     * Default value is rxBufSize
     */
    @Setter
    @Getter
    protected int appReadBufSize = 8192;

    /**
     * The application write buffer size in bytes
     * Default value is txBufSize
     */
    @Setter
    @Getter
    protected int appWriteBufSize = 8192;

    /**
     * NioChannel pool size for the endpoint,
     * this value is how many channels
     * -1 means unlimited cached, 0 means no cache
     * Default value is 500
     */
    @Setter
    @Getter
    protected int bufferPool = 500;

    /**
     * Buffer pool size in bytes to be cached
     * -1 means unlimited, 0 means no cache
     * Default value is 100MB (1024*1024*100 bytes)
     */
    @Setter
    @Getter
    protected int bufferPoolSize = 1024 * 1024 * 100;

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
    protected Integer timeToLive = 5;

    /**
     * Enable/disable SO_BROADCAST.
     *
     * <p> Some operating systems may require that the Java virtual machine be
     * started with implementation specific privileges to enable this option or
     * send broadcast datagrams.
     */
    protected Boolean soBroadcast;

    /**
     * SO_REUSEADDR option. JVM default used if not set.
     */
    @Setter
    @Getter
    protected Boolean soReuseAddress = null;

    /**
     * Disable/Enable local loopback of multicast datagrams
     * The option is used by the platform's networking code as a hint
     * for setting whether multicast data will be looped back to
     * the local socket.
     *
     * <p>Because this option is a hint, applications that want to
     * verify what loopback mode is set to should call
     * {@link #getLoopbackMode()}
     */
    @Setter
    @Getter
    protected Boolean loopbackMode = null;

    /**
     * SO_TIMEOUT option. default is 20000.
     */
    @Setter
    @Getter
    protected Integer soTimeout = 20000;

    /**
     * Traffic class option, value between 0 and 255
     * IPTOS_LOWCOST (0x02)
     * IPTOS_RELIABILITY (0x04)
     * IPTOS_THROUGHPUT (0x08)
     * IPTOS_LOWDELAY (0x10)
     * JVM default used if not set
     */
    @Setter
    @Getter
    protected Integer soTrafficClass = null;

    /**
     * The minimum frequency of the timeout interval to avoid the
     * poller going boinkers during high traffic
     */
    @Setter
    @Getter
    protected long timeoutInterval = 1000;

    /**
     * Timeout in milliseconds for an unlock to take place.
     */
    @Setter
    @Getter
    protected int unlockTimeout = 250;

    public void setProperties(MulticastSocket socket) throws IOException {
        if (rxBufSize != null) socket.setReceiveBufferSize(rxBufSize);
        if (txBufSize != null) socket.setSendBufferSize(txBufSize);
        if (soBroadcast != null) socket.setBroadcast(soBroadcast);
        if (soReuseAddress != null) socket.setReuseAddress(soReuseAddress);
        if (soTimeout != null && soTimeout >= 0) socket.setSoTimeout(soTimeout);
        if (soTrafficClass != null) socket.setTrafficClass(soTrafficClass);
        if (loopbackMode != null) socket.setLoopbackMode(loopbackMode);
        if (timeToLive != null && timeToLive >= 0 && timeToLive <= 255) socket.setTimeToLive(timeToLive);
    }

    public void setProperties(DatagramSocket socket) throws SocketException {
        if (rxBufSize != null) socket.setReceiveBufferSize(rxBufSize);
        if (txBufSize != null) socket.setSendBufferSize(txBufSize);
        if (soBroadcast != null) socket.setBroadcast(soBroadcast);
        if (soReuseAddress != null) socket.setReuseAddress(soReuseAddress);
        if (soTimeout != null && soTimeout >= 0) socket.setSoTimeout(soTimeout);
        if (soTrafficClass != null) socket.setTrafficClass(soTrafficClass);
    }

    public void setDirectBufferPool(int directBufferPool) {
        this.bufferPool = directBufferPool;
    }

}
