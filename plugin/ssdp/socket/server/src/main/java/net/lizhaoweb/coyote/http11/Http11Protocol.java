/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 09:28
 */
package net.lizhaoweb.coyote.http11;

import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.coyote.ActionCode;
import net.lizhaoweb.coyote.RequestGroupInfo;
import net.lizhaoweb.coyote.RequestInfo;
import net.lizhaoweb.mikefox.modeler.Registry;
import net.lizhaoweb.mikefox.net.AbstractEndpoint.Handler.SocketState;
import net.lizhaoweb.mikefox.net.JIoUdpEndpoint;
import net.lizhaoweb.mikefox.net.JIoUdpEndpoint.Handler;
import net.lizhaoweb.mikefox.net.MulticastSocketFactory;
import net.lizhaoweb.mikefox.net.SocketStatus;
import net.lizhaoweb.mikefox.net.SocketWrapper;

import javax.management.ObjectName;
import java.net.MulticastSocket;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract the protocol implementation, including threading, etc.
 * Processor is single threaded and specific to stream-based protocols,
 * will not fit Jk protocols like JNI.
 * <p>
 * Created by jhon on 2024/6/7 9:28
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public class Http11Protocol extends AbstractHttp11Protocol {


    // ------------------------------------------------------------ Constructor


    public Http11Protocol() {
        endpoint = new JIoUdpEndpoint();
        setSoTimeout(Constants.DEFAULT_CONNECTION_TIMEOUT);
        setTimeToLive(Constants.DEFAULT_TIME_TO_LIVE);

    }


    // ----------------------------------------------------------------- Fields


    protected Http11ConnectionHandler cHandler = new Http11ConnectionHandler(this);


    protected MulticastSocketFactory socketFactory = null;


    // ----------------------------------------- ProtocolHandler Implementation
    // *


    @Override
    public void init() throws Exception {
        endpoint.setName(getName());
        ((JIoUdpEndpoint) endpoint).setHandler(cHandler);

        // Verify the validity of the configured socket factory
        try {
            if (socketFactoryName != null) {
                socketFactory = (MulticastSocketFactory) Class.forName(socketFactoryName).newInstance();
                ((JIoUdpEndpoint) endpoint).setServerSocketFactory(socketFactory);
            }
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.socketfactory.initerror"), ex);
            throw ex;
        }

        if (socketFactory != null) {
            for (String key : attributes.keySet()) {
                Object v = attributes.get(key);
                socketFactory.setAttribute(key, v);
            }
        }

        try {
            endpoint.init();
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.endpoint.initerror"), ex);
            throw ex;
        }
        if (log.isInfoEnabled()) log.info(sm.getString("http11protocol.init", getName()));

    }

    @Override
    public void start() throws Exception {
        if (this.domain != null) {
            try {
                tpOname = new ObjectName(domain + ":" + "type=ThreadPool,name=" + getName());
                Objects.requireNonNull(Registry.getRegistry(null, null)).registerComponent(endpoint, tpOname, (String) null);
            } catch (Exception e) {
                log.error("Can't register endpoint");
            }
            rgOname = new ObjectName(domain + ":type=GlobalRequestProcessor,name=" + getName());
            Objects.requireNonNull(Registry.getRegistry(null, null)).registerComponent(cHandler.global, rgOname, (String) null);
        }

        try {
            endpoint.start();
        } catch (Exception ex) {
            log.error(sm.getString("http11protocol.endpoint.starterror"), ex);
            throw ex;
        }
        if (log.isInfoEnabled()) log.info(sm.getString("http11protocol.start", getName()));
    }


    @Override
    public void destroy() throws Exception {
        cHandler.recycledProcessors.clear();
        super.destroy();
    }
    // ------------------------------------------------------------- Properties


    /**
     * Name of the socket factory.
     */
    protected String socketFactoryName = null;

    public String getSocketFactory() {
        return socketFactoryName;
    }

    public void setSocketFactory(String valueS) {
        socketFactoryName = valueS;
    }

    // -----------------------------------  Http11ConnectionHandler Inner Class

    protected static class Http11ConnectionHandler implements Handler {

        protected Http11Protocol proto;
        protected AtomicLong registerCount = new AtomicLong(0);
        protected RequestGroupInfo global = new RequestGroupInfo();
        protected ConcurrentHashMap<SocketWrapper<MulticastSocket>, Http11Processor> connections = new ConcurrentHashMap<SocketWrapper<MulticastSocket>, Http11Processor>();

        protected ConcurrentLinkedQueue<Http11Processor> recycledProcessors = new ConcurrentLinkedQueue<Http11Processor>() {
            private static final long serialVersionUID = 1L;
            private AtomicInteger size = new AtomicInteger(0);

            @Override
            public boolean offer(Http11Processor processor) {
                boolean offer = proto.getProcessorCache() == -1 || (size.get() < proto.getProcessorCache());
                //avoid over growing our cache or add after we have stopped
                boolean result = false;
                if (offer) {
                    result = super.offer(processor);
                    if (result) {
                        size.incrementAndGet();
                    }
                }
                if (!result) unregister(processor);
                return result;
            }

            @Override
            public Http11Processor poll() {
                Http11Processor result = super.poll();
                if (result != null) {
                    size.decrementAndGet();
                }
                return result;
            }

            @Override
            public void clear() {
                Http11Processor next = poll();
                while (next != null) {
                    unregister(next);
                    next = poll();
                }
                super.clear();
                size.set(0);
            }
        };

        Http11ConnectionHandler(Http11Protocol proto) {
            this.proto = proto;
        }

        @Override
        public SocketState process(SocketWrapper<MulticastSocket> socket) {
            return process(socket, SocketStatus.OPEN);
        }

        @Override
        public SocketState process(SocketWrapper<MulticastSocket> socket, SocketStatus status) {
            Http11Processor processor = connections.remove(socket);
            boolean recycle = true;
            try {
                if (processor == null) {
                    processor = recycledProcessors.poll();
                }
                if (processor == null) {
                    processor = createProcessor();
                }
                processor.action(ActionCode.ACTION_START, null);
                SocketState state = socket.isAsync() ? processor.asyncDispatch(status) : processor.process(socket);
                if (state == SocketState.LONG) {
                    connections.put(socket, processor);
                    socket.setAsync(true);
                    recycle = false;
                } else {
                    connections.remove(socket);
                    socket.setAsync(false);
                }
                return state;
            } catch (java.net.SocketException e) {
                // SocketExceptions are normal
                Http11Protocol.log.debug(sm.getString("http11protocol.proto.socketexception.debug"), e);
            } catch (java.io.IOException e) {
                // IOExceptions are normal
                Http11Protocol.log.debug(sm.getString("http11protocol.proto.ioexception.debug"), e);
            }
            // Future developers: if you discover any other
            // rare-but-nonfatal exceptions, catch them here, and log as
            // above.
            catch (Throwable e) {
                // any other exception or error is odd. Here we log it
                // with "ERROR" level, so it will show up even on
                // less-than-verbose logs.
                Http11Protocol.log.error(sm.getString("http11protocol.proto.error"), e);
            } finally {
                //       if(proto.adapter != null) proto.adapter.recycle();
                //                processor.recycle();
                if (recycle) {
                    assert processor != null;
                    processor.action(ActionCode.ACTION_STOP, null);
                    recycledProcessors.offer(processor);
                }
            }
            return SocketState.CLOSED;
        }

        protected Http11Processor createProcessor() {
            Http11Processor processor = new Http11Processor(proto.getMaxHttpHeaderSize(), (JIoUdpEndpoint) proto.endpoint);
            processor.setAdapter(proto.adapter);
            processor.setMaxKeepAliveRequests(proto.getMaxKeepAliveRequests());
            processor.setTimeToLive(proto.getTimeToLive());
            processor.setTimeout(proto.getTimeout());
            processor.setDisableUploadTimeout(proto.isDisableUploadTimeout());
            processor.setCompressionMinSize(proto.getCompressionMinSize());
            processor.setCompression(proto.getCompression());
            processor.setNoCompressionUserAgents(proto.getNoCompressionUserAgents());
            processor.setCompressableMimeTypes(proto.getCompressableMimeTypes());
            processor.setRestrictedUserAgents(proto.getRestrictedUserAgents());
            processor.setSocketBuffer(proto.getSocketBuffer());
            processor.setMaxSavePostSize(proto.getMaxSavePostSize());
            processor.setServer(proto.getServer());
            register(processor);
            return processor;
        }

        protected void register(Http11Processor processor) {
            if (proto.getDomain() != null) {
                synchronized (this) {
                    try {
                        long count = registerCount.incrementAndGet();
                        RequestInfo rp = processor.getRequest().getRequestProcessor();
                        rp.setGlobalProcessor(global);
                        ObjectName rpName = new ObjectName(proto.getDomain() + ":type=RequestProcessor,worker=" + proto.getName() + ",name=HttpRequest" + count);
                        if (log.isDebugEnabled()) {
                            log.debug("Register " + rpName);
                        }
                        Objects.requireNonNull(Registry.getRegistry(null, null)).registerComponent(rp, rpName, null);
                        rp.setRpName(rpName);
                    } catch (Exception e) {
                        log.warn("Error registering request");
                    }
                }
            }
        }

        protected void unregister(Http11Processor processor) {
            if (proto.getDomain() != null) {
                synchronized (this) {
                    try {
                        RequestInfo rp = processor.getRequest().getRequestProcessor();
                        rp.setGlobalProcessor(null);
                        ObjectName rpName = rp.getRpName();
                        if (log.isDebugEnabled()) {
                            log.debug("Unregister " + rpName);
                        }
                        Objects.requireNonNull(Registry.getRegistry(null, null)).unregisterComponent(rpName);
                        rp.setRpName(null);
                    } catch (Exception e) {
                        log.warn("Error unregistering request", e);
                    }
                }
            }
        }

    }

}
