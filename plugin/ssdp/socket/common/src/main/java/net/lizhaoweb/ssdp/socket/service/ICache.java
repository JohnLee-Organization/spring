/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.service
 * @date : 2024-05-29
 * @time : 09:38
 */
package net.lizhaoweb.ssdp.socket.service;

import java.util.concurrent.TimeUnit;

/**
 * [接口] 缓存
 * <p>
 * Created by jhon on 2024/5/29 9:38
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public interface ICache<K, V> {

    boolean set(K key, V value, long time, TimeUnit timeUnit);

    V get(K key, V defaultValue);
}
