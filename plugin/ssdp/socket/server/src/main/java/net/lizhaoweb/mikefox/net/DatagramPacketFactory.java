/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 12:44
 */
package net.lizhaoweb.mikefox.net;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Hashtable;

/**
 * Created by jhon on 2024/6/6 12:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public abstract class DatagramPacketFactory implements Cloneable {

    private static DatagramPacketFactory theFactory;
    protected Hashtable<String, Object> attributes = new Hashtable<String, Object>();

    /**
     * Constructor is used only by subclasses.
     */
    protected DatagramPacketFactory() {
        /* NOTHING */
    }

    /**
     * General mechanism to pass attributes from the
     * ServerConnector to the socket factory.
     * <p>
     * Note that the "preferred" mechanism is to
     * use bean setters and explicit methods, but
     * this allows easy configuration via server.xml
     * or simple Properties
     */
    public void setAttribute(String name, Object value) {
        if (name != null && value != null) attributes.put(name, value);
    }

    /**
     * Returns a copy of the environment's default socket factory.
     */
    public static synchronized DatagramPacketFactory getDefault() {
        //
        // optimize typical case:  no synch needed
        //
        if (theFactory == null) {
            //
            // Different implementations of this method could
            // work rather differently.  For example, driving
            // this from a system property, or using a different
            // implementation than JavaSoft's.
            //
            theFactory = new DefaultDatagramPacketFactory();
        }
        try {
            return theFactory.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Constructs a {@code DatagramPacket} for receiving packets of
     * length {@code length}.
     * <p>
     * The {@code length} argument must be less than or equal to
     * {@code buf.length}.
     *
     * @param buf    buffer for holding the incoming datagram.
     * @param length the number of bytes to read.
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int length);

    /**
     * Constructs a {@code DatagramPacket} for receiving packets of
     * length {@code length}, specifying an offset into the buffer.
     * <p>
     * The {@code length} argument must be less than or equal to
     * {@code buf.length}.
     *
     * @param buf    buffer for holding the incoming datagram.
     * @param offset the offset for the buffer
     * @param length the number of bytes to read.
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int offset, int length);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} to the specified port number on the specified
     * host. The {@code length} argument must be less than or equal
     * to {@code buf.length}.
     *
     * @param buf     the packet data.
     * @param length  the packet length.
     * @param address the destination address.
     * @param port    the destination port number.
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int length, InetAddress address, int port);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} with offset {@code ioffset}to the
     * specified port number on the specified host. The
     * {@code length} argument must be less than or equal to
     * {@code buf.length}.
     *
     * @param buf     the packet data.
     * @param offset  the packet data offset.
     * @param length  the packet data length.
     * @param address the destination address.
     * @param port    the destination port number.
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int offset, int length, InetAddress address, int port);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} to the specified port number on the specified
     * host. The {@code length} argument must be less than or equal
     * to {@code buf.length}.
     *
     * @param buf     the packet data.
     * @param length  the packet length.
     * @param address the destination address.
     * @throws IllegalArgumentException if address type is not supported
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int length, SocketAddress address);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} with offset {@code ioffset}to the
     * specified port number on the specified host. The
     * {@code length} argument must be less than or equal to
     * {@code buf.length}.
     *
     * @param buf     the packet data.
     * @param offset  the packet data offset.
     * @param length  the packet data length.
     * @param address the destination socket address.
     * @throws IllegalArgumentException if address type is not supported
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int offset, int length, SocketAddress address);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} to the specified port number on the specified
     * host. The {@code length} argument must be less than or equal
     * to {@code buf.length}.
     *
     * @param buf      the packet data.
     * @param length   the packet length.
     * @param hostname the Host name
     * @param port     the destination port number.
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int length, String hostname, int port);

    /**
     * Constructs a datagram packet for sending packets of length
     * {@code length} with offset {@code ioffset}to the
     * specified port number on the specified host. The
     * {@code length} argument must be less than or equal to
     * {@code buf.length}.
     *
     * @param buf      the packet data.
     * @param offset   the packet data offset.
     * @param length   the packet data length.
     * @param hostname the Host name
     * @param port     the destination port number.
     * @see java.net.InetAddress
     */
    public abstract DatagramPacket createDatagramPacket(byte buf[], int offset, int length, String hostname, int port);

    public void initDatagramPacket(DatagramPacket dp) {
    }

    @Override
    public DatagramPacketFactory clone() throws CloneNotSupportedException {
        return (DatagramPacketFactory) super.clone();
    }

}
