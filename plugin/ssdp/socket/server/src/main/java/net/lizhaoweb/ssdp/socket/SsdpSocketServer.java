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
import net.lizhaoweb.ssdp.service.ISsdpReceiver;
import net.lizhaoweb.ssdp.socket.config.ServerSsdpConfiguration;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketCreateException;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketDataReceiveException;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketJoinGroupException;
import net.lizhaoweb.ssdp.socket.exception.MulticastSocketLeaveGroupException;
import net.lizhaoweb.ssdp.socket.handler.IServiceHandler;
import net.lizhaoweb.ssdp.socket.listener.IServerEvent;
import net.lizhaoweb.ssdp.socket.listener.IServerLifeListener;
import net.lizhaoweb.ssdp.socket.listener.SsdpServerListenerManager;
import net.lizhaoweb.ssdp.socket.service.HandlerThread;

import java.net.InetAddress;
import java.net.MulticastSocket;
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
public class SsdpSocketServer implements ISsdpServer, ISsdpReceiver<IServerContext>, Runnable {

//    /**
//     * SSDP服务器配置
//     */
//    private ServerSsdpConfiguration config;

    /**
     * SSDP服务器应用对象
     */
    private ServerApplication application;

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

    public SsdpSocketServer(@NotNull final ServerSsdpConfiguration config) {
        System.out.println("Instantiate server ...");
        serverStatus = 0x00;
        Collection<IServerLifeListener> instantiateListeners = SsdpServerListenerManager.getServerInstantiate();
        if (instantiateListeners != null && instantiateListeners.size() > 0) {
            for (IServerLifeListener listener : instantiateListeners) {
                if (listener == null) {
                    continue;
                }
                listener.onPre(new IServerEvent() {
                    @Getter
                    private ServerSsdpConfiguration _config = config.clone();
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
                    private ServerSsdpConfiguration _config = config.clone();
                    @Getter
                    private short _serverStatus = serverStatus;
                });
            }
        }
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
                    private ServerSsdpConfiguration _config = config.clone();
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
        System.out.println("Init server ...");
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
        if (this.application.getConfig().getHandlerList() != null && this.application.getConfig().getHandlerList().size() > 0) {
            for (IServiceHandler handler : this.application.getConfig().getHandlerList()) {
                if (handler == null || handler.getMethod() == null) {
                    continue;
                }
                this.application.registerHandler(handler);
            }
        }
        this.threadPool = new ThreadPoolExecutor(16, 128, 1000, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(16));

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
        System.out.println("Start server ...");
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
        System.out.println("Stop server ...");
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
        System.out.println("Close server ...");
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
        System.out.println("Destroy server ...");
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
        System.out.println("Run server ...");
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
        socket = this.buildMulticastSocket(this.application.getGroupInetAddress(), this.application.getGroupPort());

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
                IServerContext context = this.receive();
                System.out.println("Receive a packet connection request: " + context.getDatagramPacket().getAddress().toString());

                HandlerThread thread = new HandlerThread(context);
                threadPool.execute(thread);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } while (0x32 == serverStatus);
    }

    @Override
    public IServerContext receive() {
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

    // 构建组播套节子
    private MulticastSocket buildMulticastSocket(InetAddress group, int port) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(port);
        } catch (Exception e) {
            throw new MulticastSocketCreateException(e);
        }
        try {
            socket.joinGroup(group);
        } catch (Exception e) {
            throw new MulticastSocketJoinGroupException(e);
        }
        return socket;
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
