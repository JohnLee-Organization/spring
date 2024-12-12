/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-06
 * @time : 17:18
 */
package net.lizhaoweb.coyote.http11;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.coyote.Adapter;
import net.lizhaoweb.coyote.ProtocolHandler;
import net.lizhaoweb.mikefox.modeler.Registry;
import net.lizhaoweb.mikefox.net.AbstractEndpoint;
import net.lizhaoweb.mikefox.res.StringManager;
import sun.security.action.GetPropertyAction;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * Created by jhon on 2024/6/6 17:18
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractHttp11Protocol implements ProtocolHandler, MBeanRegistration {

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);

    protected ObjectName tpOname = null;
    protected ObjectName rgOname = null;

    protected AbstractEndpoint endpoint = null;

    /**
     * The adapter, used to call the connector.
     */
    @Setter
    @Getter
    protected Adapter adapter;


    protected HashMap<String, Object> attributes = new HashMap<String, Object>();


    /**
     * Pass config info
     */
    public void setAttribute(String name, Object value) {
        if (log.isTraceEnabled()) {
            log.trace(sm.getString("http11protocol.setattribute", name, value));
        }
        attributes.put(name, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public Iterator<String> getAttributeNames() {
        return attributes.keySet().iterator();
    }

    /**
     * Set a property.
     */
    public boolean setProperty(String name, String value) {
        setAttribute(name, value); //store all settings
        if (name != null && (name.startsWith("socket.") || name.startsWith("selectorPool."))) {
            return endpoint.setProperty(name, value);
        } else {
            return endpoint.setProperty(name, value); //make sure we at least try to set all properties
        }

    }

    /**
     * Get a property
     */
    public String getProperty(String name) {
        return (String) getAttribute(name);
    }

    public InetAddress getAddress() {
        return endpoint.getAddress();
    }

    public void setAddress(InetAddress ia) {
        endpoint.setAddress(ia);
        setAttribute("address", "" + ia);
    }

    public String getName() {
        String encodedAddr = "";
        if (getAddress() != null) {
            encodedAddr = "" + getAddress();
            if (encodedAddr.startsWith("/")) encodedAddr = encodedAddr.substring(1);
            try {
                String dfltEncName = AccessController.doPrivileged(new GetPropertyAction("file.encoding"));
                encodedAddr = URLEncoder.encode(encodedAddr, dfltEncName) + "-";
            } catch (UnsupportedEncodingException e) {
                // The system should always have the platform default
            }
        }
        return ("http-" + encodedAddr + endpoint.getPort());
    }


    public void pause() throws Exception {
        try {
            endpoint.pause();
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.endpoint.pauseerror"), ex);
            throw ex;
        }
        if (log.isInfoEnabled()) log.info(sm.getString("http11protocol.pause", getName()));
    }

    public void resume() throws Exception {
        try {
            endpoint.resume();
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.endpoint.resumeerror"), ex);
            throw ex;
        }
        if (log.isInfoEnabled()) log.info(sm.getString("http11protocol.resume", getName()));
    }

    public void destroy() throws Exception {
        if (log.isInfoEnabled()) log.info(sm.getString("http11protocol.stop", getName()));
        endpoint.destroy();
        if (tpOname != null) Objects.requireNonNull(Registry.getRegistry(null, null)).unregisterComponent(tpOname);
        if (rgOname != null) Objects.requireNonNull(Registry.getRegistry(null, null)).unregisterComponent(rgOname);
    }

    /**
     * Processor cache.
     */
    @Setter
    @Getter
    private int processorCache = 200;

    @Setter
    @Getter
    private int socketBuffer = 9000;

    // HTTP

    /**
     * Maximum number of requests which can be performed over a keepalive
     * connection. The default is the same as for Apache HTTP Server.
     */
    public int getMaxKeepAliveRequests() {
        return endpoint.getMaxKeepAliveRequests();
    }

    public void setMaxKeepAliveRequests(int mkar) {
        endpoint.setMaxKeepAliveRequests(mkar);
        setAttribute("maxKeepAliveRequests", "" + mkar);
    }

    /**
     * Return the Keep-Alive policy for the connection.
     */
    public boolean getKeepAlive() {
        return ((endpoint.getMaxKeepAliveRequests() != 0) && (endpoint.getMaxKeepAliveRequests() != 1));
    }

    /**
     * Set the keep-alive policy for this connection.
     */
    public void setKeepAlive(boolean keepAlive) {
        if (!keepAlive) {
            setMaxKeepAliveRequests(1);
        }
    }

    public int getTimeout() {
        return getSoTimeout();
    }

    public void setTimeout(int timeout) {
        setSoTimeout(timeout);
    }

    public int getConnectionTimeout() {
        return getSoTimeout();
    }

    public void setConnectionTimeout(int timeout) {
        setSoTimeout(timeout);
    }

    public int getSoTimeout() {
        return endpoint.getSoTimeout();
    }

    public void setSoTimeout(int i) {
        endpoint.setSoTimeout(i);
        setAttribute("soTimeout", "" + i);
        setAttribute("timeout", "" + i);
        setAttribute("connectionTimeout", "" + i);
    }

    public int getTimeToLive() {
        return endpoint.getTimeToLive();
    }

    public void setTimeToLive(int i) {
        endpoint.setTimeToLive(i);
    }

    // *
    /**
     * Maximum size of the post which will be saved when processing certain
     * requests, such as a POST.
     */
    @Setter
    @Getter
    private int maxSavePostSize = 4 * 1024;


    // HTTP
    /**
     * Maximum size of the HTTP message header.
     */
    @Setter
    @Getter
    private int maxHttpHeaderSize = 8 * 1024;


    // HTTP
    /**
     * If true, the regular socket timeout will be used for the full duration
     * of the connection.
     */
    @Setter
    @Getter
    private boolean disableUploadTimeout = true;


    // HTTP
    /**
     * Integrated compression support.
     */
    @Setter
    @Getter
    private String compression = "off";


    // HTTP
    @Setter
    @Getter
    private String noCompressionUserAgents = null;
    // HTTP
    private String compressableMimeTypes = "text/html,text/xml,text/plain";

    public String getCompressableMimeType() {
        return compressableMimeTypes;
    }

    public void setCompressableMimeType(String valueS) {
        compressableMimeTypes = valueS;
    }

    public String getCompressableMimeTypes() {
        return getCompressableMimeType();
    }

    public void setCompressableMimeTypes(String valueS) {
        setCompressableMimeType(valueS);
    }

    // HTTP
    @Setter
    @Getter
    private int compressionMinSize = 2048;

    // HTTP
    /**
     * User agents regular expressions which should be restricted to HTTP/1.0 support.
     */
    @Setter
    @Getter
    private String restrictedUserAgents = null;

    // HTTP
    /**
     * Server header.
     */
    @Setter
    @Getter
    private String server;

    public Executor getExecutor() {
        return endpoint.getExecutor();
    }

    public void setExecutor(Executor executor) {
        endpoint.setExecutor(executor);
    }


    public int getMaxThreads() {
        return endpoint.getMaxThreads();
    }

    public void setMaxThreads(int maxThreads) {
        endpoint.setMaxThreads(maxThreads);
    }


    public int getThreadPriority() {
        return endpoint.getThreadPriority();
    }

    public void setThreadPriority(int threadPriority) {
        endpoint.setThreadPriority(threadPriority);
    }

    public int getPort() {
        return endpoint.getPort();
    }

    public void setPort(int port) {
        endpoint.setPort(port);
    }

    public abstract void init() throws Exception;

    public abstract void start() throws Exception;

    // -------------------- JMX related methods --------------------

    // *
    @Getter
    protected String domain;
    @Getter
    protected ObjectName objectName;
    protected MBeanServer mserver;

    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        objectName = name;
        mserver = server;
        domain = name.getDomain();
        return name;
    }

    public void postRegister(Boolean registrationDone) {
    }

    public void preDeregister() throws Exception {
    }

    public void postDeregister() {
    }

}
