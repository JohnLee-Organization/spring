/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 10:49
 */
package net.lizhaoweb.spring.zk.queue;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>守望者 - 生产</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
class ProduceWatcher implements Watcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Object mutex;

    public ProduceWatcher(Object mutex) {
        this.mutex = mutex;
    }

    @Override
    public void process(WatchedEvent event) {
        //----------------------------------------------------
        // 生产一条消息成功后通知一个等待线程
        //----------------------------------------------------
        synchronized (mutex) {
            mutex.notify();
        }
    }
}
