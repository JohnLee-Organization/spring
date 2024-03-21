/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket
 * @date : 2024-03-07
 * @time : 11:35
 */
package net.lizhaoweb.ssdp.socket;

import com.sun.istack.internal.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.ssdp.ISsdpServer;
import net.lizhaoweb.ssdp.exception.SsdpIOException;
import net.lizhaoweb.ssdp.exception.SsdpUnknownHostException;
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.socket.config.ServerConfiguration;
import net.lizhaoweb.ssdp.socket.exception.*;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import net.lizhaoweb.ssdp.socket.listener.IServerEvent;
import net.lizhaoweb.ssdp.socket.listener.IServerLifeListener;
import net.lizhaoweb.ssdp.socket.listener.SsdpServerListenerManager;
import net.lizhaoweb.ssdp.socket.service.HandlerThread;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.*;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * SSDP服务器
 * <p>
 * 实例化 -> 初始化(init) -> 开始(start) -> 运行(run) -> 停止(stop) -> 关闭(close) -> 销毁(destroy)
 * <p>
 * Created by Jhon.Lee on 3/7/2024 11:35
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings({"unused"})
public class SsdpSocketServer implements ISsdpServer, ISsdpReceiver<SsdpRequest, IServerContext>, Runnable {

    /**
     * SSDP服务器配置
     */
    private ServerConfiguration config;

    /**
     * SSDP服务器应用对象
     */
    private IServerApplication application;

    // 用来存放socket连接
    private ThreadPoolExecutor threadPool;
    private MulticastSocket socket;

    private volatile int threadStatus = 0;

    /**
     * 服务器状态。
     * <p>
     * 0x00：开始实例化；0x01：实例化；0x02：已经实例化；
     * 0x10：准备初始化；0x11：初始化；0x12：已经初始化；
     * 0x20：准备启动；0x21：启动；0x22：已启动；
     * 0x32：运行中；
     * 0x80：准备停止；0x81：停止；0x82：已经停止；
     * 0x90：准备关闭；0x91：关闭；0x92：已经关闭；
     * 0xA0：准备销毁；0xA1：销毁；0xA2：已经销毁；
     */
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private short serverStatus;

    public SsdpSocketServer(@NotNull final ServerConfiguration config) {
        log.error("Instantiate server ...");
        serverStatus = 0x00;
        Collection<IServerLifeListener> instantiateListeners = SsdpServerListenerManager.getServerInstantiate();
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ServerConfiguration _config = config.clone();
                    @Getter
                    private short _serverStatus = serverStatus;
                });
            }
        }

        serverStatus = 0x01;
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ServerConfiguration _config = config.clone();
                    @Getter
                    private short _serverStatus = serverStatus;
                });
            }
        }
        this.config = config;
        this.application = new ServerApplication(config);

        serverStatus = 0x02;
        this.application.setServerStatus(serverStatus);
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ServerConfiguration _config = config.clone();
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
    }

    @Override
    public void init() {
        log.error("Init server ...");
        serverStatus = 0x10;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> initializationListeners = SsdpServerListenerManager.getServerInitialization();
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x11;
        this.application.setServerStatus(serverStatus);
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
        if (this.config.getHandlerList() != null && this.config.getHandlerList().size() > 0) {
            for (IServiceHandler<IServerContext, SsdpRequest, SsdpResponse> handler : this.config.getHandlerList()) {
                if (handler == null || handler.getMethod() == null) {
                    continue;
                }
                this.application.registerHandler(handler);
            }
        }
        this.threadPool = new ThreadPoolExecutor(16, 128, 1000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(16));
        String hostname = "239.255.255.250";//TODO hostname
        if (StringUtils.isNotBlank(this.config.getBroadcastAddress())) {
            hostname = this.config.getBroadcastAddress();
        }
        try {
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(String hostname, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress addr, int port);
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(int port);
//        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostname, port);
            InetAddress groupInetAddress = InetAddress.getByName(hostname);
            this.application.setGroupInetAddress(groupInetAddress);
        } catch (UnknownHostException e) {
            throw new SsdpUnknownHostException(e);
        }
        int groupPort = 1900;//TODO port
        if (this.config.getBroadcastPort() > 1024) {
            groupPort = this.config.getBroadcastPort();
        }
        this.application.setGroupPort(groupPort);

        serverStatus = 0x12;
        this.application.setServerStatus(serverStatus);
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
    }

    @Override
    public synchronized void start() {
        log.error("Start server ...");
        serverStatus = 0x20;
        this.application.setServerStatus(serverStatus);
        if (threadStatus != 0) {
            throw new IllegalThreadStateException();
        }
        Collection<IServerLifeListener> startListeners = SsdpServerListenerManager.getServerStart();
        if (startListeners != null && startListeners.size() > 0) {
            for (IServerLifeListener listener : startListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x21;
        this.application.setServerStatus(serverStatus);
        if (startListeners != null && startListeners.size() > 0) {
            for (IServerLifeListener listener : startListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x22;
        this.application.setServerStatus(serverStatus);
        if (startListeners != null && startListeners.size() > 0) {
            for (IServerLifeListener listener : startListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

    }

    @Override
    public synchronized void stop() {
        log.error("Stop server ...");
        if (serverStatus != 0x32) {
            throw new IllegalThreadStateException();
        }

        serverStatus = 0x80;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> stopListeners = SsdpServerListenerManager.getServerStop();
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x81;
        this.application.setServerStatus(serverStatus);
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
        while (!Thread.currentThread().isInterrupted()) {
            Thread.currentThread().interrupt();
        }
        this.leaveGroup(socket);

        serverStatus = 0x82;
        this.application.setServerStatus(serverStatus);
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        this.close();
    }

    @Override
    public synchronized void close() {
        log.error("Close server ...");
        serverStatus = 0x90;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> closeListeners = SsdpServerListenerManager.getServerClose();
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x91;
        this.application.setServerStatus(serverStatus);
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
        this.closeMulticastSocket(socket);
        this.closeThreadPoolExecutor(threadPool);
        application.close();

        serverStatus = 0x92;
        this.application.setServerStatus(serverStatus);
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        this.destroy();
    }

    @Override
    public synchronized void destroy() {
        log.error("Destroy server ...");
        serverStatus = 0xA0;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> destroyListeners = SsdpServerListenerManager.getServerDestroy();
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0xA1;
        this.application.setServerStatus(serverStatus);
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
        this.application.destroy();

        serverStatus = 0xA2;
        this.application.setServerStatus(serverStatus);
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
    }

    @Override
    public void run() {
        log.error("Run server ...");
        if (serverStatus != 0x22) {
            throw new IllegalThreadStateException();
        }

        serverStatus = 0x30;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> runListeners = SsdpServerListenerManager.getServerRun();
        if (runListeners != null && runListeners.size() > 0) {
            for (IServerLifeListener listener : runListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }

        serverStatus = 0x31;
        this.application.setServerStatus(serverStatus);
        if (runListeners != null && runListeners.size() > 0) {
            for (IServerLifeListener listener : runListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new IServerEvent() {
                    @Getter
                    private ThreadPoolExecutor _threadPool = threadPool;
                    @Getter
                    private short _serverStatus = serverStatus;
                    @Getter
                    private IServerApplication _application = application;
                });
            }
        }
        socket = this.buildMulticastSocket(this.application.getGroupInetAddress(), this.application.getGroupPort(), this.config.getTimeToLive(), this.config.getSoTimeout());


        do {
            if (0x31 == serverStatus) {
                serverStatus = 0x32;
                this.application.setServerStatus(serverStatus);
                if (runListeners != null && runListeners.size() > 0) {
                    for (IServerLifeListener listener : runListeners) {
                        if (listener == null) {
                            continue;
                        }
                        try {
                            listener.onPos(new IServerEvent() {
                                @Getter
                                private ThreadPoolExecutor _threadPool = threadPool;
                                @Getter
                                private short _serverStatus = serverStatus;
                                @Getter
                                private IServerApplication _application = application;
                            });
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
                continue;
            }

            try {
                IServerContext context = this.receive(null);
                log.trace("Receive a packet connection request: " + context.getDatagramPacket().getSocketAddress());

                HandlerThread thread = new HandlerThread(context);
                threadPool.execute(thread);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } while (0x32 == serverStatus);
    }

    @Override
    public IServerContext receive(SsdpRequest request) {
        IServerContext context = new ServerContext(this.application);
        try {
            socket.receive(context.getDatagramPacket());
        } catch (Exception e) {
            throw new MulticastSocketDataReceiveException(e);
        }
        return context;
    }

    private void leaveGroup(MulticastSocket serverSocket) {
        if (serverSocket == null) {
            return;
        }
        InetAddress inetAddress = application.getGroupInetAddress();
        if (serverSocket.getInetAddress() != null) {
            inetAddress = serverSocket.getInetAddress();
        }
        try {
            log.debug("leaveGroup = {}", inetAddress);
            serverSocket.leaveGroup(inetAddress);
        } catch (Exception e) {
            throw new MulticastSocketLeaveGroupException(e);
        }
    }

//    private MulticastSocket accept(MulticastSocket serverSocket, ServerContext context) {
//        try {
//            serverSocket.receive(context.getDatagramPacket());
//        } catch (Exception e) {
//            throw new MulticastSocketDataReceiveException(e);
//        }
//        return this.buildMulticastSocket(serverSocket.getInetAddress(), serverSocket.getPort());
//    }

    /**
     * 构建组播套节子
     *
     * @param groupInetAddress 组播地址
     * @param groupPort        组播端口
     * @param timeToLive       设置此套节子发送的组播数据包的默认生存时间，以控制组播的范围。
     *                         ttl必须在0<=ttl<=255的范围内，否则将引发IllegalArgumentException。
     *                         以TTL为0发送的组播数据包不在网络上传输，而是可以在本地传递。
     * @param soTimeout        指定的超时时间（以毫秒为单位），来启用/禁用SO_TIMEOUT选项。
     *                         如果此选项设置为非零时，则套节子的receive()方法将被阻塞到此时间量，
     *                         超过此时间量，虽然套节子仍然有效，但会抛出java.net.SocketTimeoutException。
     *                         必须在进入阻止操作之前启用该选项才能生效。超时值必须大于0。超时为零被解释为无限超时。
     * @return 组播套节子
     */
    private MulticastSocket buildMulticastSocket(InetAddress groupInetAddress, int groupPort, int timeToLive, int soTimeout) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(groupPort);
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        try {
            socket.joinGroup(groupInetAddress);
        } catch (Exception e) {
            throw new MulticastSocketJoinGroupException(e);
        }
        this.socketSetTimeToLive(socket, timeToLive);
        this.socketSetSoTimeout(socket, soTimeout);
        return socket;
    }

    /**
     * 组播套节子设置数据包的生存时间
     *
     * @param socket     组播套节子
     * @param timeToLive 设置此套节子发送的组播数据包的默认生存时间，以控制组播的范围。
     *                   ttl必须在0<=ttl<=255的范围内，否则将引发IllegalArgumentException。
     *                   以TTL为0发送的组播数据包不在网络上传输，而是可以在本地传递。
     */
    private void socketSetTimeToLive(MulticastSocket socket, int timeToLive) {
        try {
            if (timeToLive >= 0 && timeToLive <= 255) {
                socket.setTimeToLive(timeToLive);
            }
        } catch (SocketException e) {
            throw new MulticastSocketException(e);
        } catch (IOException e) {
            throw new SsdpIOException(e);
        }
    }

    /**
     * 数据套节子设置超时时间
     *
     * @param socket    数据套节子
     * @param soTimeout 指定的超时时间（以毫秒为单位），来启用/禁用SO_TIMEOUT选项。
     *                  如果此选项设置为非零时，则套节子的receive()方法将被阻塞到此时间量，
     *                  超过此时间量，虽然套节子仍然有效，但会抛出java.net.SocketTimeoutException。
     *                  必须在进入阻止操作之前启用该选项才能生效。超时值必须大于0。超时为零被解释为无限超时。
     */
    private void socketSetSoTimeout(DatagramSocket socket, int soTimeout) {
        try {
            socket.setSoTimeout(soTimeout);
        } catch (SocketException e) {
            throw new MulticastSocketException(e);
        }
    }

    private void closeMulticastSocket(MulticastSocket socket) {
        while (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;
    }

    private void closeThreadPoolExecutor(ThreadPoolExecutor executor) {
        while (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        executor = null;
    }
}