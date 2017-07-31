/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.common
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 22:22
 */
package net.lizhaoweb.spring.zk.common;

import lombok.Getter;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <h1>客户端 - ZooKeeper</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月17日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ZooKeeperClient implements IZooKeeperClient {

    @Getter
    private ZooKeeper zooKeeper;

//    private static String connectionString = "localhost:2181";
//    private static int sessionTimeout = 10000;
//
//
//    public static ZooKeeper getInstance() throws IOException, InterruptedException {
//        //--------------------------------------------------------------
//        // 为避免连接还未完成就执行zookeeper的get/create/exists操作引起的（KeeperErrorCode = ConnectionLoss)
//        // 这里等Zookeeper的连接完成才返回实例
//        //--------------------------------------------------------------
//        final CountDownLatch connectedSignal = new CountDownLatch(1);
//        ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new ZooKeeperStatusWatcher());
//        connectedSignal.await(sessionTimeout, TimeUnit.MILLISECONDS);
//        return zooKeeper;
//    }
//
//    public static int getSessionTimeout() {
//        return sessionTimeout;
//    }
//
//    public static void setSessionTimeout(int sessionTimeout) {
//        ZooKeeperClient.sessionTimeout = sessionTimeout;
//    }

    public ZooKeeperClient(CountDownLatch countDownLatch, ZooKeeper zooKeeper, int sessionTimeout) {
        this.zooKeeper = zooKeeper;
        try {
            countDownLatch.await(sessionTimeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
