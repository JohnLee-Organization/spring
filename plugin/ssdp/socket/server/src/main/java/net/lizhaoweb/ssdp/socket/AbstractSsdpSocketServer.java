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
import net.lizhaoweb.ssdp.model.dto.SsdpRequest;
import net.lizhaoweb.ssdp.model.dto.SsdpResponse;
import net.lizhaoweb.ssdp.service.IMessageFactory;
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.socket.config.ServerConfig;
import net.lizhaoweb.ssdp.socket.exception.*;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import net.lizhaoweb.ssdp.socket.listener.IServerLifeListener;
import net.lizhaoweb.ssdp.socket.listener.SsdpServerListenerManager;
import net.lizhaoweb.ssdp.socket.listener.impl.ServerLifeEvent;
import net.lizhaoweb.ssdp.socket.model.ServerStatus;
import net.lizhaoweb.ssdp.socket.service.HandlerThread;
import net.lizhaoweb.ssdp.socket.service.IMessageCache;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static net.lizhaoweb.ssdp.socket.model.ServerStatus.*;

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
public abstract class AbstractSsdpSocketServer implements ISsdpServer, ISsdpReceiver<SsdpRequest, IServerContext>, Runnable {

    private IMessageCache<String, SsdpRequest> requestCache;

    /**
     * SSDP服务器配置
     */
    private ServerConfig config;

    /**
     * SSDP服务器应用对象
     */
    private IServerApplication application;

    // 用来存放socket连接
    private ThreadPoolExecutor threadPool;
    private MulticastSocket receiveSocket;

    private volatile int threadStatus = 0;

    /**
     * 服务器状态。
     * <p>
     * PRE_INSTANCE：开始实例化；INSTANCING：实例化；INSTANCED：已经实例化；
     * PRE_INITIALIZE：准备初始化；INITIALIZING：初始化；INITIALIZED：已经初始化；
     * PRE_START：准备启动；STARTING：启动；STARTED：已启动；
     * PRE_RUN：准备运行, RUNNING：运行中；
     * PRE_STOP：准备停止；STOPPING：停止；STOPPED：已经停止；
     * PRE_CLOSE：准备关闭；CLOSING：关闭；CLOSED：已经关闭；
     * PRE_DESTROY：准备销毁；DESTROYING：销毁；DESTROYED：已经销毁；
     */
    @Setter(AccessLevel.PACKAGE)
    @Getter
    private ServerStatus serverStatus;

    public AbstractSsdpSocketServer(@NotNull final ServerConfig config, IMessageFactory messageFactory) {
        System.out.println("Instantiate server ...");
        serverStatus = PRE_INSTANCE;
        Collection<IServerLifeListener> instantiateListeners = SsdpServerListenerManager.getServerInstantiate();
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(config.clone(), serverStatus));
            }
        }

        serverStatus = INSTANCING;
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(config.clone(), serverStatus));
            }
        }
        this.config = config;
        this.application = this.buildApplication(config, messageFactory);

        serverStatus = INSTANCED;
        this.application.setServerStatus(serverStatus);
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application));
            }
        }
    }

    @Override
    public void init() {
        System.out.println("Init server ...");
        serverStatus = PRE_INITIALIZE;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> initializationListeners = SsdpServerListenerManager.getServerInitialization();
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(serverStatus, application));
            }
        }

        serverStatus = INITIALIZING;
        this.application.setServerStatus(serverStatus);
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application));
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
        this.initGroupInetAddress(this.config, this.application);
        int groupPort = 1900;//TODO port
        if (this.config.getBroadcastPort() > 1024) {
            groupPort = this.config.getBroadcastPort();
        }
        this.application.setGroupPort(groupPort);

        serverStatus = INITIALIZED;
        this.application.setServerStatus(serverStatus);
        if (initializationListeners != null && initializationListeners.size() > 0) {
            for (IServerLifeListener listener : initializationListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
    }

    @Override
    public synchronized void start() {
        System.out.println("Start server ...");
        serverStatus = PRE_START;
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
                listener.onPre(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = STARTING;
        this.application.setServerStatus(serverStatus);
        if (startListeners != null && startListeners.size() > 0) {
            for (IServerLifeListener listener : startListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = STARTED;
        this.application.setServerStatus(serverStatus);
        if (startListeners != null && startListeners.size() > 0) {
            for (IServerLifeListener listener : startListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

    }

    @Override
    public synchronized void stop() {
        System.out.println("Stop server ...");
        if (serverStatus != RUNNING) {
            throw new IllegalThreadStateException();
        }

        serverStatus = PRE_STOP;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> stopListeners = SsdpServerListenerManager.getServerStop();
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = STOPPING;
        this.application.setServerStatus(serverStatus);
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
        while (!Thread.currentThread().isInterrupted()) {
            Thread.currentThread().interrupt();
        }
        this.leaveGroup(receiveSocket);

        serverStatus = STOPPED;
        this.application.setServerStatus(serverStatus);
        if (stopListeners != null && stopListeners.size() > 0) {
            for (IServerLifeListener listener : stopListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
    }

    @Override
    public synchronized void close() {
        System.out.println("Close server ...");
        serverStatus = PRE_CLOSE;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> closeListeners = SsdpServerListenerManager.getServerClose();
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = CLOSING;
        this.application.setServerStatus(serverStatus);
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
        this.closeMulticastSocket(receiveSocket);
        this.closeThreadPoolExecutor(threadPool);
        application.close();

        serverStatus = CLOSED;
        this.application.setServerStatus(serverStatus);
        if (closeListeners != null && closeListeners.size() > 0) {
            for (IServerLifeListener listener : closeListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
    }

    @Override
    public synchronized void destroy() {
        System.out.println("Destroy server ...");
        serverStatus = PRE_DESTROY;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> destroyListeners = SsdpServerListenerManager.getServerDestroy();
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = DESTROYING;
        this.application.setServerStatus(serverStatus);
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
        this.application.destroy();

        serverStatus = DESTROYED;
        this.application.setServerStatus(serverStatus);
        if (destroyListeners != null && destroyListeners.size() > 0) {
            for (IServerLifeListener listener : destroyListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
    }

    public boolean isDestroyed() {
        return serverStatus == DESTROYED;
    }

    @Override
    public void run() {
        System.out.println("Run server ...");
        if (serverStatus != STARTED) {
            throw new IllegalThreadStateException();
        }

        serverStatus = PRE_RUN;
        this.application.setServerStatus(serverStatus);
        Collection<IServerLifeListener> runListeners = SsdpServerListenerManager.getServerRun();
        if (runListeners != null && runListeners.size() > 0) {
            for (IServerLifeListener listener : runListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }

        serverStatus = PRE_RUN;
        this.application.setServerStatus(serverStatus);
        if (runListeners != null && runListeners.size() > 0) {
            for (IServerLifeListener listener : runListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onExe(new ServerLifeEvent(serverStatus, application, threadPool));
            }
        }
        receiveSocket = this.buildMulticastSocket(this.application.getGroupPort());
        this.socketJoinGroup(receiveSocket, this.application.getGroupInetAddress());
        this.socketSetTimeToLive(receiveSocket, this.config.getTimeToLive());
        this.socketSetSoTimeout(receiveSocket, this.config.getSoTimeout());


        do {
            if (PRE_RUN == serverStatus) {
                serverStatus = RUNNING;
                this.application.setServerStatus(serverStatus);
                if (runListeners != null && runListeners.size() > 0) {
                    for (IServerLifeListener listener : runListeners) {
                        if (listener == null) {
                            continue;
                        }
                        try {
                            listener.onPos(new ServerLifeEvent(serverStatus, application, threadPool));
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
        } while (RUNNING == serverStatus);
    }

    @Override
    public IServerContext receive(SsdpRequest request) {
        IServerContext context = new ServerContext(this.application);
        try {
            receiveSocket.receive(context.getDatagramPacket());
        } catch (Exception e) {
            throw new MulticastSocketDataReceiveException(e);
        }
        return context;
    }

    /**
     * 构建服务器应用对象
     *
     * @param config         服务器配置对象
     * @param messageFactory 消息工厂
     * @return IServerApplication
     */
    protected abstract IServerApplication buildApplication(ServerConfig config, IMessageFactory messageFactory);

    /**
     * 初始化组播地址
     *
     * @param config      服务器配置对象
     * @param application 服务器应用对象
     */
    protected abstract void initGroupInetAddress(ServerConfig config, IServerApplication application);

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

    /**
     * 构建组播套节子
     *
     * @param socketPort 套节子端口
     * @return 组播套节子
     */
    private MulticastSocket buildMulticastSocket(int socketPort) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(socketPort);
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        return socket;
    }

    /**
     * 组播套节子加入组
     *
     * @param socket           组播套节子
     * @param groupInetAddress 组播地址
     */
    private void socketJoinGroup(MulticastSocket socket, InetAddress groupInetAddress) {
        try {
            socket.joinGroup(groupInetAddress);
        } catch (Exception e) {
            throw new MulticastSocketJoinGroupException(e);
        }
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
