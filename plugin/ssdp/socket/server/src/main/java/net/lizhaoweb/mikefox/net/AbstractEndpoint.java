/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.net
 * @date : 2024-06-06
 * @time : 11:56
 */
package net.lizhaoweb.mikefox.net;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.mikefox.res.StringManager;
import net.lizhaoweb.mikefox.threads.ResizableExecutor;
import net.lizhaoweb.mikefox.threads.TaskQueue;
import net.lizhaoweb.mikefox.threads.TaskThreadFactory;
import net.lizhaoweb.mikefox.threads.ThreadPoolExecutor;
import net.lizhaoweb.mikefox.util.IntrospectionUtils;

import java.io.File;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jhon on 2024/6/6 11:56
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractEndpoint {

    // -------------------------------------------------------------- Constants
    protected static final StringManager sm = StringManager.getManager("net.lizhaoweb.mikefox.net.res");

    /**
     * Different types of socket states to react upon
     */
    public static interface Handler {
        public enum SocketState {
            OPEN, CLOSED, LONG
        }
    }

//    // Standard SSL Configuration attributes
//    // JSSE
//    // Standard configuration attribute names
//    public static final String SSL_ATTR_ALGORITHM = "algorithm";
//    public static final String SSL_ATTR_CLIENT_AUTH = "clientAuth";
//    public static final String SSL_ATTR_KEYSTORE_FILE = "keystoreFile";
//    public static final String SSL_ATTR_KEYSTORE_PASS = "keystorePass";
//    public static final String SSL_ATTR_KEYSTORE_TYPE = "keystoreType";
//    public static final String SSL_ATTR_KEYSTORE_PROVIDER = "keystoreProvider";
//    public static final String SSL_ATTR_SSL_PROTOCOL = "sslProtocol";
//    public static final String SSL_ATTR_CIPHERS = "ciphers";
//    public static final String SSL_ATTR_CIPHERS_ARRAY = "ciphersArray";
//    public static final String SSL_ATTR_KEY_ALIAS = "keyAlias";
//    public static final String SSL_ATTR_KEY_PASS = "keyPass";
//    public static final String SSL_ATTR_TRUSTSTORE_FILE = "truststoreFile";
//    public static final String SSL_ATTR_TRUSTSTORE_PASS = "truststorePass";
//    public static final String SSL_ATTR_TRUSTSTORE_TYPE = "truststoreType";
//    public static final String SSL_ATTR_TRUSTSTORE_PROVIDER = "truststoreProvider";
//    public static final String SSL_ATTR_TRUSTSTORE_ALGORITHM = "truststoreAlgorithm";
//    public static final String SSL_ATTR_CRL_FILE = "crlFile";
//    public static final String SSL_ATTR_TRUST_MAX_CERT_LENGTH = "trustMaxCertLength";
//    public static final String SSL_ATTR_SESSION_CACHE_SIZE = "sessionCacheSize";
//    public static final String SSL_ATTR_SESSION_TIMEOUT = "sessionTimeout";
//    public static final String SSL_ATTR_ALLOW_UNSAFE_RENEG = "allowUnsafeLegacyRenegotiation";

    // ----------------------------------------------------------------- Fields


    /**
     * Running state of the endpoint.
     */
    @Getter
    protected volatile boolean running = false;


    /**
     * Will be set to true whenever the endpoint is paused.
     */
    @Getter
    protected volatile boolean paused = false;

    /**
     * Track the initialization state of the endpoint.
     */
    protected boolean initialized = false;

    /**
     * Are we using an internal executor
     */
    protected volatile boolean internalExecutor = false;

    /**
     * Socket properties
     */
    @Getter
    protected SocketProperties socketProperties = new SocketProperties();


    // ----------------------------------------------------------------- Properties
    /**
     * External Executor based thread pool.
     */
    @Getter
    private Executor executor = null;

    public void setExecutor(Executor executor) {
        this.executor = executor;
        this.internalExecutor = (executor == null);
    }


    /**
     * Server socket port.
     */
    @Setter
    @Getter
    private int port;


    /**
     * Address for the server socket.
     */
    @Setter
    @Getter
    private InetAddress address;


    /**
     * Socket timeout.
     */
    public int getSoTimeout() {
        return socketProperties.getSoTimeout();
    }

    public void setSoTimeout(int soTimeout) {
        socketProperties.setSoTimeout(soTimeout);
    }


    /**
     * Socket time-to-live.
     */
    public int getTimeToLive() {
        return socketProperties.getTimeToLive();
    }

    public void setTimeToLive(int timeToLive) {
        socketProperties.setTimeToLive(timeToLive);
    }

//    /**
//     * SSL engine.
//     */
//    @Setter
//    @Getter
//    private boolean SSLEnabled = false;


    private int minSpareThreads = 10;

    public int getMinSpareThreads() {
        return Math.min(minSpareThreads, getMaxThreads());
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
        if (running && executor != null) {
            if (executor instanceof java.util.concurrent.ThreadPoolExecutor) {
                ((java.util.concurrent.ThreadPoolExecutor) executor).setCorePoolSize(maxThreads);
            } else if (executor instanceof ResizableExecutor) {
                ((ResizableExecutor) executor).resizePool(minSpareThreads, maxThreads);
            }
        }
    }

    /**
     * Maximum amount of worker threads.
     */
    private int maxThreads = 200;

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
        if (running && executor != null) {
            if (executor instanceof java.util.concurrent.ThreadPoolExecutor) {
                ((java.util.concurrent.ThreadPoolExecutor) executor).setMaximumPoolSize(maxThreads);
            } else if (executor instanceof ResizableExecutor) {
                ((ResizableExecutor) executor).resizePool(minSpareThreads, maxThreads);
            }
        }
    }

    public int getMaxThreads() {
        if (running && executor != null) {
            if (executor instanceof java.util.concurrent.ThreadPoolExecutor) {
                return ((java.util.concurrent.ThreadPoolExecutor) executor).getMaximumPoolSize();
            } else if (executor instanceof ResizableExecutor) {
                return ((ResizableExecutor) executor).getMaxThreads();
            } else {
                return -1;
            }
        } else {
            return maxThreads;
        }
    }

    /**
     * Max keep alive requests
     */
    @Setter
    @Getter
    private int maxKeepAliveRequests = 100; // as in Apache HTTPD server

    /**
     * Name of the thread pool, which will be used for naming child threads.
     */
    @Setter
    @Getter
    private String name = "TP";

    /**
     * The default is true - the created threads will be
     * in daemon mode. If set to false, the control thread
     * will not be daemon - and will keep the process alive.
     */
    @Setter
    @Getter
    private boolean daemon = true;


    /**
     * Priority of the worker threads.
     */
    @Setter
    @Getter
    protected int threadPriority = Thread.NORM_PRIORITY;


    /**
     * Generic properties, introspected
     */
    public boolean setProperty(String name, String value) {
        final String socketName = "socket.";
        try {
            if (name.startsWith(socketName)) {
                return IntrospectionUtils.setProperty(socketProperties, name.substring(socketName.length()), value);
            } else {
                return IntrospectionUtils.setProperty(this, name, value, false);
            }
        } catch (Exception x) {
            log.error("Unable to set attribute \"" + name + "\" to \"" + value + "\"", x);
            return false;
        }
    }


    public void createExecutor() {
        internalExecutor = true;
        TaskQueue taskQueue = new TaskQueue();
        TaskThreadFactory tf = new TaskThreadFactory(getName() + "-exec-", daemon, getThreadPriority());
        executor = new ThreadPoolExecutor(getMinSpareThreads(), getMaxThreads(), 60L, TimeUnit.SECONDS, taskQueue, tf);
        taskQueue.setParent((ThreadPoolExecutor) executor);
    }

    public void shutdownExecutor() {
        if (executor != null && internalExecutor) {
            if (executor instanceof ThreadPoolExecutor) {
                //this is our internal one, so we need to shut it down
                ThreadPoolExecutor tpe = (ThreadPoolExecutor) executor;
                tpe.shutdownNow();
                TaskQueue queue = (TaskQueue) tpe.getQueue();
                queue.setParent(null);
            }
            executor = null;
        }
    }

    /**
     * Unlock the server socket accept using a bogus connection.
     */
    protected void unlockAccept() {
        DatagramSocket socket = null;
        InetSocketAddress sAddr = null;
        try {
            // Need to create a connection to unlock the accept();
            if (address == null) {
                sAddr = new InetSocketAddress("localhost", getPort());
            } else {
                sAddr = new InetSocketAddress(address, getPort());
            }
            socket = new DatagramSocket();
            socket.setSoTimeout(getSocketProperties().getSoTimeout());
            // TODO Consider hard-coding to s.setSoLinger(true,0)
            socket.connect(sAddr);
            if (log.isDebugEnabled()) {
                log.debug("Socket unlock completed for:" + sAddr);
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("endpoint.debug.unlock", "" + getPort()), e);
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    public abstract void pause();

    public abstract void resume();

    public abstract void start() throws Exception;

    public abstract void destroy() throws Exception;

    public abstract void init() throws Exception;

    public String adjustRelativePath(String path, String relativeTo) {
        File file = new File(path);
        if (!file.isAbsolute()) {
            path = relativeTo + File.separator + path;
            file = new File(path);
        }
        if (!file.exists()) {
            log.warn("configured file:[" + path + "] does not exist.");
        }
        return path;
    }

    public String defaultIfNull(String val, String defaultValue) {
        if (val == null) return defaultValue;
        return val;
    }
//
//    // --------------------  SSL related properties --------------------
//
//    @Setter
//    @Getter
//    private String algorithm = KeyManagerFactory.getDefaultAlgorithm();
//
//    @Setter
//    @Getter
//    private String clientAuth = "false";
//
//    @Getter
//    private String keystoreFile = System.getProperty("user.home") + "/.keystore";
//
//    public void setKeystoreFile(String s) {
//        this.keystoreFile = adjustRelativePath(s, System.getProperty("quentin.base"));
//    }
//
//    @Setter
//    @Getter
//    private String keystorePass = null;
//
//    @Setter
//    @Getter
//    private String keystoreType = "JKS";
//
//
//    @Setter
//    @Getter
//    private String keystoreProvider = null;
//
//
//    @Setter
//    @Getter
//    private String sslProtocol = "TLS";
//
//    // Note: Some implementations use the comma separated string, some use
//    // the array
//    @Getter
//    private String ciphers = null;
//
//    @Getter
//    private String[] ciphersArray = new String[0];
//
//    public void setCiphers(String s) {
//        ciphers = s;
//        if (s == null) ciphersArray = new String[0];
//        else {
//            StringTokenizer t = new StringTokenizer(s, ",");
//            ciphersArray = new String[t.countTokens()];
//            for (int i = 0; i < ciphersArray.length; i++) ciphersArray[i] = t.nextToken();
//        }
//    }
//
//
//    @Setter
//    @Getter
//    private String keyAlias = null;
//
//    @Setter
//    @Getter
//    private String keyPass = JSSESocketFactory.DEFAULT_KEY_PASS;
//
//    @Getter
//    private String truststoreFile = System.getProperty("javax.net.ssl.trustStore");
//
//    public void setTruststoreFile(String s) {
//        this.truststoreFile = adjustRelativePath(s, System.getProperty("quentin.base"));
//    }
//
//
//    @Setter
//    @Getter
//    private String truststorePass = System.getProperty("javax.net.ssl.trustStorePassword");
//
//    @Setter
//    @Getter
//    private String truststoreType = System.getProperty("javax.net.ssl.trustStoreType");
//
//    @Setter
//    @Getter
//    private String truststoreProvider = null;
//
//    @Setter
//    @Getter
//    private String truststoreAlgorithm = null;
//
//    @Setter
//    @Getter
//    private String crlFile = null;
//
//    @Setter
//    @Getter
//    private String trustMaxCertLength = null;
//
//    @Setter
//    @Getter
//    private String sessionCacheSize = null;
//
//    @Setter
//    @Getter
//    private String sessionTimeout = "86400";
//
//    @Setter
//    @Getter
//    private String allowUnsafeLegacyRenegotiation = null;
//
//
//    private String sslEnabledProtocols = null; //"TLSv1,SSLv3,SSLv2Hello"
//    @Getter
//    private String[] sslEnabledProtocolsArray = new String[0];
//
//    public void setSslEnabledProtocols(String s) {
//        this.sslEnabledProtocols = s;
//        StringTokenizer t = new StringTokenizer(s, ",");
//        sslEnabledProtocolsArray = new String[t.countTokens()];
//        for (int i = 0; i < sslEnabledProtocolsArray.length; i++) sslEnabledProtocolsArray[i] = t.nextToken();
//    }

}
