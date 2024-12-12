/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 11:58
 */
package net.lizhaoweb.mikefox.net;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.quentin.Globals;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RejectedExecutionException;

/**
 * Handle incoming UDP connections.
 * <p>
 * This class implement a simple server model: one listener thread accepts on a socket and
 * creates a new worker thread for each incoming connection.
 * <p>
 * More advanced Endpoints will reuse the threads, use queues, etc.
 * <p>
 * Created by jhon on 2024/6/6 11:58
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public class JIoUdpEndpoint extends AbstractUdpEndpoint {

    // ----------------------------------------------------------------- Fields

    /**
     * Associated server socket.
     */
    protected MulticastSocket serverSocket = null;

    protected ConcurrentLinkedQueue<SocketWrapper<MulticastSocket>> waitingRequests = new ConcurrentLinkedQueue<>();


    // ------------------------------------------------------------- Properties

//    /**
//     * Generic properties - currently only socket.xxx properties
//     */
//    @Override
//    public boolean setProperty(String name, String value) {
//        final String socketName = "socket.";
//        try {
//            if (name.startsWith(socketName)) {
//                return IntrospectionUtils.setProperty(socketProperties, name.substring(socketName.length()), value);
//            }
//            return super.setProperty(name, value);
//        } catch (Exception x) {
//            log.error("Unable to set attribute \"" + name + "\" to \"" + value + "\"", x);
//            return false;
//        }
//    }

    /**
     * Acceptor thread count.
     */
    @Setter
    @Getter
    protected int acceptorThreadCount = 0;

    /**
     * Handling of accepted sockets.
     */
    @Setter
    @Getter
    protected Handler handler = null;

    /**
     * Server socket factory.
     */
    @Setter
    @Getter
    protected MulticastSocketFactory serverSocketFactory = null;

    @Setter
    @Getter
    protected DatagramPacketFactory datagramPacketFactory = null;


    // ------------------------------------------------ Handler Inner Interface


    /**
     * Bare bones interface used for socket processing. Per thread data is to be
     * stored in the ThreadWithAttributes extra folders, or alternately in
     * thread local fields.
     */
    public interface Handler {
        net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState process(SocketWrapper<MulticastSocket> socket);

        net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState process(SocketWrapper<MulticastSocket> socket, SocketStatus status);
    }


    // ------------------------------------------- SocketProcessor Inner Class


    /**
     * This class is the equivalent of the Worker, but will simply use in an
     * external Executor thread pool.
     */
    protected class MulticastSocketProcessor implements Runnable {

        protected SocketWrapper<MulticastSocket> socket = null;
        protected SocketStatus status = null;

        public MulticastSocketProcessor(SocketWrapper<MulticastSocket> socket) {
            if (socket == null) throw new NullPointerException();
            this.socket = socket;
        }

        public MulticastSocketProcessor(SocketWrapper<MulticastSocket> socket, SocketStatus status) {
            this(socket);
            this.status = status;
        }

        public void run() {
            boolean launch = false;
            try {
                if (!socket.processing.compareAndSet(false, true)) {
                    log.error("Unable to process socket. Invalid state.");
                    return;
                }

                net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState state = net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.OPEN;
                // Process the request from this socket
                if ((!socket.isInitialized()) && (!setSocketOptions(socket.getSocket()))) {
                    state = net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.CLOSED;
                }
                socket.setInitialized(true);

                if ((state != net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.CLOSED)) {
                    state = (status == null) ? handler.process(socket) : handler.process(socket, status);
                }
                if (state == net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.CLOSED) {
                    // Close socket
                    if (log.isTraceEnabled()) {
                        log.trace("Closing socket:" + socket);
                    }
                    socket.getSocket().close();
                } else if (state == net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.OPEN) {
                    socket.setKeptAlive(true);
                    socket.access();
                    //keepalive connection
                    //TODO - servlet3 check async status, we may just be in a hold pattern
                    launch = true;
                } else if (state == net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState.LONG) {
                    socket.access();
                    waitingRequests.add(socket);
                }
            } finally {
                socket.processing.set(false);
                if (launch) getExecutor().execute(new MulticastSocketProcessor(socket));
                socket = null;
            }
            // Finish up this request
        }

    }


    // -------------------- Public methods --------------------

    @Override
    public void init() throws Exception {
        if (initialized) return;

        // Initialize thread count defaults for acceptor
        if (acceptorThreadCount == 0) {
            acceptorThreadCount = 1;
        }
        if (serverSocketFactory == null) {
            serverSocketFactory = MulticastSocketFactory.getDefault();
        }
//        if (isSSLEnabled()) {
//            serverSocketFactory.setAttribute(SSL_ATTR_ALGORITHM, getAlgorithm());
//            serverSocketFactory.setAttribute(SSL_ATTR_CLIENT_AUTH, getClientAuth());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEYSTORE_FILE, getKeystoreFile());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEYSTORE_PASS, getKeystorePass());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEYSTORE_TYPE, getKeystoreType());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEYSTORE_PROVIDER, getKeystoreProvider());
//            serverSocketFactory.setAttribute(SSL_ATTR_SSL_PROTOCOL, getSslProtocol());
//            serverSocketFactory.setAttribute(SSL_ATTR_CIPHERS, getCiphers());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEY_ALIAS, getKeyAlias());
//            serverSocketFactory.setAttribute(SSL_ATTR_KEY_PASS, getKeyPass());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUSTSTORE_FILE, getTruststoreFile());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUSTSTORE_PASS, getTruststorePass());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUSTSTORE_TYPE, getTruststoreType());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUSTSTORE_PROVIDER, getTruststoreProvider());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUSTSTORE_ALGORITHM, getTruststoreAlgorithm());
//            serverSocketFactory.setAttribute(SSL_ATTR_CRL_FILE, getCrlFile());
//            serverSocketFactory.setAttribute(SSL_ATTR_TRUST_MAX_CERT_LENGTH, getTrustMaxCertLength());
//            serverSocketFactory.setAttribute(SSL_ATTR_SESSION_CACHE_SIZE, getSessionCacheSize());
//            serverSocketFactory.setAttribute(SSL_ATTR_SESSION_TIMEOUT, getSessionTimeout());
//            serverSocketFactory.setAttribute(SSL_ATTR_ALLOW_UNSAFE_RENEG, getAllowUnsafeLegacyRenegotiation());
//        }

        if (datagramPacketFactory == null) {
            datagramPacketFactory = DatagramPacketFactory.getDefault();
        }

        if (serverSocket == null) {
            try {
                if (getAddress() == null) {
                    serverSocket = serverSocketFactory.createSocket(getPort());
                } else {
                    serverSocket = serverSocketFactory.createSocket(getPort(), getAddress());
                }
            } catch (BindException orig) {
                String msg;
                if (getAddress() == null) msg = orig.getMessage() + " <null>:" + getPort();
                else msg = orig.getMessage() + " " + getAddress().toString() + ":" + getPort();
                BindException be = new BindException(msg);
                be.initCause(orig);
                throw be;
            }
        }

        initialized = true;

    }

    @Override
    public void start() throws Exception {
        // Initialize socket if not done before
        if (!initialized) {
            init();
        }
        if (!running) {
            running = true;
            paused = false;

            // Create worker collection
            if (getExecutor() == null) {
                createExecutor();
            }

            // Start acceptor threads
            for (int i = 0; i < acceptorThreadCount; i++) {
                Thread acceptorThread = new Thread(new UdpAcceptor(), getName() + "-Acceptor-" + i);
                acceptorThread.setPriority(threadPriority);
                acceptorThread.setDaemon(isDaemon());
                acceptorThread.start();
            }
        }
    }

    @Override
    public void pause() {
        if (running && !paused) {
            paused = true;
            unlockAccept();
        }
    }

    @Override
    public void resume() {
        if (running) {
            paused = false;
        }
    }

    public void stop() {
        if (running) {
            running = false;
            unlockAccept();
        }
        shutdownExecutor();
    }

    /**
     * Deallocate APR memory pools, and close server socket.
     */
    @Override
    public void destroy() throws Exception {
        if (running) {
            stop();
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                log.error(sm.getString("endpoint.err.close"), e);
            }
            serverSocket = null;
        }
        initialized = false;
    }


    /**
     * Set the options for the current socket.
     */
    protected boolean setSocketOptions(MulticastSocket socket) {
        // Process the connection
        try {
            // 1: Set socket options: timeout, linger, etc
            socketProperties.setProperties(socket);
        } catch (SocketException s) {
            //error here is common if the client has reset the connection
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("endpoint.err.unexpected"), s);
            }
            // Close the socket
            return false;
        } catch (Throwable t) {
            log.error(sm.getString("endpoint.err.unexpected"), t);
            // Close the socket
            return false;
        }
        return true;
    }


    /**
     * Process given socket.
     */
    protected boolean processSocket(DatagramPacket datagramPacket) {
        try {
            MulticastSocket socket = new MulticastSocket();
            SocketWrapper<MulticastSocket> wrapper = new SocketWrapper<>(datagramPacket, socket);
            wrapper.setKeepAliveLeft(getMaxKeepAliveRequests());
            getExecutor().execute(new MulticastSocketProcessor(wrapper));
        } catch (RejectedExecutionException x) {
            log.warn("Socket processing request was rejected for:" + datagramPacket, x);
            return false;
        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
        return true;
    }

    public boolean processSocket(SocketWrapper<MulticastSocket> socket, SocketStatus status) {
        try {
            if (status == SocketStatus.OPEN || status == SocketStatus.STOP || status == SocketStatus.TIMEOUT) {
                if (waitingRequests.remove(socket)) {
                    MulticastSocketProcessor proc = new MulticastSocketProcessor(socket, status);
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    try {
                        //threads should not be created by the webapp classloader
                        if (Globals.IS_SECURITY_ENABLED) {
                            PrivilegedAction<Void> pa = new PrivilegedSetTccl(getClass().getClassLoader());
                            AccessController.doPrivileged(pa);
                        } else {
                            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                        }
                        getExecutor().execute(proc);
                    } finally {
                        if (Globals.IS_SECURITY_ENABLED) {
                            PrivilegedAction<Void> pa = new PrivilegedSetTccl(loader);
                            AccessController.doPrivileged(pa);
                        } else {
                            Thread.currentThread().setContextClassLoader(loader);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // This means we got an OOM or similar creating a thread, or that
            // the pool and its queue are full
            log.error(sm.getString("endpoint.process.fail"), t);
            return false;
        }
        return true;
    }

    protected class UdpAcceptor implements Runnable {

        /**
         * The background thread that listens for incoming TCP/IP connections and
         * hands them off to an appropriate processor.
         */
        @Override
        public void run() {

            // Loop until we receive a shutdown command
            while (running) {

                // Loop if endpoint is paused
                while (paused) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                // Accept the next incoming connection from the server socket
                try {
                    DatagramPacket dp = datagramPacketFactory.createDatagramPacket(dataBuffer, dataOffset, dataLength, destAddress, destPort);
                    serverSocket.receive(dp);
                    datagramPacketFactory.initDatagramPacket(dp);
                    // Hand this socket off to an appropriate processor
                    if (!processSocket(dp)) {
                        //                        // Close socket right away
//                        try {
//                            dp.close();
//                        } catch (IOException e) {
//                            // Ignore
//                        }
                    }
                } catch (IOException x) {
                    if (running) log.error(sm.getString("endpoint.accept.fail"), x);
                } catch (Throwable t) {
                    log.error(sm.getString("endpoint.accept.fail"), t);
                }
                // The processor will recycle itself when it finishes
            }

        }

    }

    private static class PrivilegedSetTccl implements PrivilegedAction<Void> {

        private ClassLoader cl;

        PrivilegedSetTccl(ClassLoader cl) {
            this.cl = cl;
        }

        public Void run() {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
        }
    }

}
