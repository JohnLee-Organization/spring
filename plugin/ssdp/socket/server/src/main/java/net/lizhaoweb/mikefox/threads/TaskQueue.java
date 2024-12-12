/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.threads
 * @date : 2024-06-06
 * @time : 13:11
 */
package net.lizhaoweb.mikefox.threads;

import lombok.Setter;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * As task queue specifically designed to run with a thread pool executor.
 * The task queue is optimised to properly utilize threads within
 * a thread pool executor. If you use a normal queue, the executor will spawn threads
 * when there are idle threads and you wont be able to force items unto the queue itself
 * <p>
 * Created by jhon on 2024/6/6 13:11
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class TaskQueue extends LinkedBlockingQueue<Runnable> {

    @Setter
    private ThreadPoolExecutor parent = null;

    public TaskQueue() {
        super();
    }

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public TaskQueue(Collection<? extends Runnable> c) {
        super(c);
    }

    public boolean force(Runnable o) {
        if (parent.isShutdown()) throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        return super.offer(o); //forces the item onto the queue, to be used if the task is rejected
    }

    public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (parent.isShutdown()) throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        return super.offer(o, timeout, unit); //forces the item onto the queue, to be used if the task is rejected
    }

    @Override
    public boolean offer(Runnable o) {
        //we can't do any checks
        if (parent == null) return super.offer(o);
        //we are maxed out on threads, simply queue the object
        if (parent.getPoolSize() == parent.getMaximumPoolSize()) return super.offer(o);
        //we have idle threads, just add it to the queue
        if (parent.getActiveCount() < (parent.getPoolSize())) return super.offer(o);
        //if we have less threads than maximum force creation of a new thread
        if (parent.getPoolSize() < parent.getMaximumPoolSize()) return false;
        //if we reached here, we need to add it to the queue
        return super.offer(o);
    }

}
