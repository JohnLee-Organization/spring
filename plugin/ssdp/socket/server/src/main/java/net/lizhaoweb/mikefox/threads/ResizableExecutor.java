/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.threads
 * @date : 2024-06-06
 * @time : 13:09
 */
package net.lizhaoweb.mikefox.threads;

import java.util.concurrent.Executor;

/**
 * Created by jhon on 2024/6/6 13:09
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface ResizableExecutor extends Executor {

    /**
     * {@link java.util.concurrent.ThreadPoolExecutor#getPoolSize()}
     *
     * @return {@link java.util.concurrent.ThreadPoolExecutor#getPoolSize()}
     */
    int getPoolSize();

    int getMaxThreads();

    /**
     * {@link java.util.concurrent.ThreadPoolExecutor#getActiveCount()}
     *
     * @return {@link java.util.concurrent.ThreadPoolExecutor#getActiveCount()}
     */
    int getActiveCount();

    boolean resizePool(int corePoolSize, int maximumPoolSize);

    boolean resizeQueue(int capacity);

}
