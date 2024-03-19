/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.listener
 * @date : 2024-03-08
 * @time : 09:50
 */
package net.lizhaoweb.ssdp.socket.listener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [监听器] 管理器
 * <p>
 * Created by Jhon.Lee on 3/8/2024 09:50
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused"})
public class SsdpServerListenerManager {

    private static final class ServerLife {
        private static final String INSTANTIATE = "SERVER-INSTANTIATE"; // 实例化
        private static final String INITIALIZATION = "SERVER-INITIALIZATION"; // 初始化
        private static final String START = "SERVER-START"; // 开始
        private static final String RUN = "SERVER-RUN"; // 运行
        private static final String STOP = "SERVER-STOP"; // 停止
        private static final String CLOSE = "SERVER-CLOSE"; // 关闭
        private static final String DESTROY = "SERVER-DESTROY"; // 销毁
    }

    private static final Map<String, Map<String, IServerLifeListener>> lifeListenerMap = new ConcurrentHashMap<>();

    static {
        lifeListenerMap.put(ServerLife.INSTANTIATE, new ConcurrentHashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.INITIALIZATION, new ConcurrentHashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.START, new ConcurrentHashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.RUN, new HashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.STOP, new HashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.CLOSE, new HashMap<String, IServerLifeListener>());
        lifeListenerMap.put(ServerLife.DESTROY, new HashMap<String, IServerLifeListener>());
    }


    public static void setServerInstantiate(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.INSTANTIATE);
        putServerLifeListener(ServerLife.INSTANTIATE, listener);
    }

    public static void addServerInstantiate(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.INSTANTIATE, listener);
    }

    public static Collection<IServerLifeListener> getServerInstantiate() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.INSTANTIATE);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerInitialization(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.INITIALIZATION);
        putServerLifeListener(ServerLife.INITIALIZATION, listener);
    }

    public static void addServerInitialization(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.INITIALIZATION, listener);
    }

    public static Collection<IServerLifeListener> getServerInitialization() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.INITIALIZATION);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerStart(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.START);
        putServerLifeListener(ServerLife.START, listener);
    }

    public static void addServerStart(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.START, listener);
    }

    public static Collection<IServerLifeListener> getServerStart() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.START);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerRun(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.RUN);
        putServerLifeListener(ServerLife.RUN, listener);
    }

    public static void addServerRun(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.RUN, listener);
    }

    public static Collection<IServerLifeListener> getServerRun() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.RUN);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerStop(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.STOP);
        putServerLifeListener(ServerLife.STOP, listener);
    }

    public static void addServerStop(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.STOP, listener);
    }

    public static Collection<IServerLifeListener> getServerStop() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.STOP);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerClose(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.CLOSE);
        putServerLifeListener(ServerLife.CLOSE, listener);
    }

    public static void addServerClose(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.CLOSE, listener);
    }

    public static Collection<IServerLifeListener> getServerClose() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.CLOSE);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    public static void setServerDestroy(IServerLifeListener listener) {
        removeServerLifeListener(ServerLife.DESTROY);
        putServerLifeListener(ServerLife.DESTROY, listener);
    }

    public static void addServerDestroy(IServerLifeListener listener) {
        putServerLifeListener(ServerLife.DESTROY, listener);
    }

    public static Collection<IServerLifeListener> getServerDestroy() {
        Collection<IServerLifeListener> collection = getServerLifeListener(ServerLife.DESTROY);
        if (collection == null) {
            return null;
        }
        return Collections.unmodifiableCollection(collection);
    }


    private static void putServerLifeListener(String namespace, IServerLifeListener value) {
        putServerLifeListener(namespace, value.getClass().getName(), value);
    }

    private static void putServerLifeListener(String namespace, String key, IServerLifeListener value) {
        if (!lifeListenerMap.containsKey(namespace) || lifeListenerMap.get(namespace) == null) {
            lifeListenerMap.put(namespace, new HashMap<String, IServerLifeListener>());
        }
        lifeListenerMap.get(namespace).put(key, value);
    }

    private static Collection<IServerLifeListener> getServerLifeListener(String namespace) {
        Map<String, IServerLifeListener> serverLifeListenerMap = lifeListenerMap.get(namespace);
        if (serverLifeListenerMap == null) {
            return null;
        }
        return serverLifeListenerMap.values();
    }

    private static IServerLifeListener getServerLifeListener(String namespace, String key) {
        Map<String, IServerLifeListener> serverLifeListenerMap = lifeListenerMap.get(namespace);
        if (serverLifeListenerMap == null) {
            return null;
        }
        return serverLifeListenerMap.get(key);
    }

    private static Map<String, IServerLifeListener> removeServerLifeListener(String namespace) {
        Map<String, IServerLifeListener> remove = lifeListenerMap.remove(namespace);
        lifeListenerMap.put(namespace, new HashMap<String, IServerLifeListener>());
        return remove;
    }

    private static IServerLifeListener removeServerLifeListener(String namespace, String key) {
        if (!lifeListenerMap.containsKey(namespace) || lifeListenerMap.get(namespace) == null) {
            lifeListenerMap.put(namespace, new HashMap<String, IServerLifeListener>());
        }
        return lifeListenerMap.get(namespace).remove(key);
    }

    private static void clearServerLifeListener(String namespace) {
        if (!lifeListenerMap.containsKey(namespace) || lifeListenerMap.get(namespace) == null) {
            lifeListenerMap.put(namespace, new HashMap<String, IServerLifeListener>());
        }
        lifeListenerMap.get(namespace).clear();
    }
}
