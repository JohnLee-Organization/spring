/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:35
 */
package net.lizhaoweb.spring.zk.queue;

import lombok.Setter;
import net.lizhaoweb.spring.zk.common.IZooKeeperClient;
import net.lizhaoweb.spring.zk.common.ZooKeeperClient;
import net.lizhaoweb.spring.zk.common.ZooKeeperStatusWatcher;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ZooKeeperQueueA {

    private ZooKeeper zk;

    private static byte[] ROOT_QUEUE_DATA = {0x12, 0x34};
    private static String QUEUE_ROOT = "/QUEUE";
    private String queuePath;
    private Object mutex = new Object();

    @Setter
    private IZooKeeperClient zooKeeperClient;

    public ZooKeeperQueueA(String queueName) {
        try {
            int sessionTimeout = 10000;
            String connectionString = "localhost:2181";
            CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zooKeeper = new ZooKeeper(connectionString, sessionTimeout, new ZooKeeperStatusWatcher(countDownLatch));

            IZooKeeperClient zooKeeperClient = new ZooKeeperClient(countDownLatch, zooKeeper, sessionTimeout);

            this.queuePath = QUEUE_ROOT + "/" + queueName;
            this.zk = zooKeeper;

            //----------------------------------------------------
            // 确保队列根目录/QUEUE和当前队列的目录的存在
            //----------------------------------------------------
            ensureExists(QUEUE_ROOT);
            ensureExists(queuePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] consume() throws InterruptedException, KeeperException, UnsupportedEncodingException {
        List<String> nodes = null;
        byte[] returnVal = null;
        Stat stat = null;
        do {
            synchronized (mutex) {

                nodes = zk.getChildren(queuePath, new ProduceWatcher());

                //----------------------------------------------------
                // 如果没有消息节点，等待生产者的通知
                //----------------------------------------------------
                if (nodes == null || nodes.size() == 0) {
                    mutex.wait();
                } else {

                    SortedSet<String> sortedNode = new TreeSet<String>();
                    for (String node : nodes) {
                        sortedNode.add(queuePath + "/" + node);
                    }

                    //----------------------------------------------------
                    // 消费队列里序列号最小的消息
                    //----------------------------------------------------
                    String first = sortedNode.first();
                    returnVal = zk.getData(first, false, stat);
                    zk.delete(first, -1);

                    System.out.print(Thread.currentThread().getName() + " ");
                    System.out.print("consume a message from queue：" + first);
                    System.out.println(", message data is: " + new String(returnVal, "UTF-8"));

                    return returnVal;
                }
            }
        } while (true);
    }

    class ProduceWatcher implements Watcher {
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

    public void produce(byte[] data) throws KeeperException, InterruptedException, UnsupportedEncodingException {

        //----------------------------------------------------
        // 确保当前队列目录存在
        // example: /QUEUE/queueName
        //----------------------------------------------------
        ensureExists(queuePath);

        String node = zk.create(queuePath + "/", data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT_SEQUENTIAL);


        System.out.print(Thread.currentThread().getName() + " ");
        System.out.print("produce a message to queue：" + node);
        System.out.println(" , message data is: " + new String(data, "UTF-8"));
    }


    public void ensureExists(String path) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                zk.create(path, ROOT_QUEUE_DATA, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
