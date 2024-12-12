/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 12:58
 */
package net.lizhaoweb.mikefox.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Hashtable;

/**
 * This class creates server sockets.  It may be subclassed by other
 * factories, which create particular types of server sockets.  This
 * provides a general framework for the addition of public socket-level
 * functionality.  It it is the server side analogue of a socket factory,
 * and similarly provides a way to capture a variety of policies related
 * to the sockets being constructed.
 *
 * <P> Like socket factories, Server Socket factory instances have two
 * categories of methods.  First are methods used to create sockets.
 * Second are methods which set properties used in the production of
 * sockets, such as networking options.  There is also an environment
 * specific default server socket factory; frameworks will often use
 * their own customized factory.
 *
 * <P><hr><em> It may be desirable to move this interface into the
 * <b>java.net</b> package, so that is not an extension but the preferred
 * interface.  Should this be serializable, making it a JavaBean which can
 * be saved along with its networking configuration?
 * <p>
 * Created by jhon on 2024/6/6 12:58
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public abstract class MulticastSocketFactory implements Cloneable {

    //
    // NOTE:  JDK 1.1 bug in class GC, this can get collected
    // even though it's always accessible via getDefault().
    //
    private static MulticastSocketFactory theFactory;
    protected Hashtable<String, Object> attributes = new Hashtable<String, Object>();

    /**
     * Constructor is used only by subclasses.
     */
    protected MulticastSocketFactory() {
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
    public static synchronized MulticastSocketFactory getDefault() {
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
            theFactory = new DefaultMulticastSocketFactory();
        }
        try {
            return theFactory.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Returns a multicast socket which uses all network interfaces on
     * the host, and is bound to a the specified port.  The socket is
     * configured with the socket options (such as accept timeout)
     * given to this factory.
     *
     * @param port the port to listen to
     * @throws IOException            for networking errors
     * @throws InstantiationException for construction errors
     */
    public abstract MulticastSocket createSocket(int port) throws IOException, InstantiationException;

    /**
     * Returns a multicast socket which uses all network interfaces on
     * the host, is bound to a the specified port, and uses the
     * specified connection backlog.  The socket is configured with
     * the socket options (such as accept timeout) given to this factory.
     *
     * @param bindAddr Socket address to bind to, or null for an unbound socket.
     * @throws IOException            for networking errors
     * @throws InstantiationException for construction errors
     */
    public abstract MulticastSocket createSocket(SocketAddress bindAddr) throws IOException, InstantiationException;

    /**
     * Returns a multicast socket which uses only the specified network
     * interface on the local host, is bound to a the specified port,
     * and uses the specified connection backlog.  The socket is configured
     * with the socket options (such as accept timeout) given to this factory.
     *
     * @param port     the port to listen to
     * @param bindAddr the network interface address to use
     * @throws IOException            for networking errors
     * @throws InstantiationException for construction errors
     */
    public abstract MulticastSocket createSocket(int port, InetAddress bindAddr) throws IOException, InstantiationException;

    /**
     * Returns a multicast socket which uses only the specified network
     * interface on the local host, is bound to a the specified port,
     * and uses the specified connection backlog.  The socket is configured
     * with the socket options (such as accept timeout) given to this factory.
     *
     * @param port     the port to listen to
     * @param hostname the Host name
     * @throws IOException            for networking errors
     * @throws InstantiationException for construction errors
     */
    public abstract MulticastSocket createSocket(int port, String hostname) throws IOException, InstantiationException;

    public void initSocket(Socket s) {
    }

    @Override
    public MulticastSocketFactory clone() throws CloneNotSupportedException {
        return (MulticastSocketFactory) super.clone();
    }

}
