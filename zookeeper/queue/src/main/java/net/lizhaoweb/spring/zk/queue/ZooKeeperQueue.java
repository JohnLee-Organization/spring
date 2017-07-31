/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 22:17
 */
package net.lizhaoweb.spring.zk.queue;

import net.lizhaoweb.spring.zk.common.IZooKeeperClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月17日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ZooKeeperQueue implements IZooKeeperQueue {

    private static final String QUEUE_ROOT = "/QUEUE";
    private static final byte[] ROOT_QUEUE_DATA = {0x12, 0x34};

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ZooKeeper zk;
    //    private int sessionTimeout;
    //    private String queueName;
    private String queuePath;
    private Object mutex = new Object();

    public ZooKeeperQueue(IZooKeeperClient zooKeeperClient, String queueName) {
//        this.queueName = queueName;
        this.queuePath = QUEUE_ROOT + "/" + queueName;
        this.zk = zooKeeperClient.getZooKeeper();
//        this.sessionTimeout = zk.getSessionTimeout();

        //----------------------------------------------------
        // 确保队列根目录/QUEUE和当前队列的目录的存在
        //----------------------------------------------------
        this.ensureExists(QUEUE_ROOT);
        this.ensureExists(this.queuePath);
    }

    public byte[] consume() {
        try {
            List<String> nodes = null;
            byte[] returnVal = null;
            Stat stat = null;
            do {
                synchronized (mutex) {

                    nodes = zk.getChildren(queuePath, new ProduceWatcher(mutex));

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

                        logger.info("{} consume a message from queue：{}, message data is: {}", Thread.currentThread().getName(), first, new String(returnVal, "UTF-8"));
                        System.out.print(Thread.currentThread().getName() + " ");
                        System.out.print("consume a message from queue：" + first);
                        System.out.println(", message data is: " + new String(returnVal,"UTF-8"));

                        return returnVal;
                    }
                }
            } while (true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void produce(byte[] data) {
        try {
            //----------------------------------------------------
            // 确保当前队列目录存在
            // example: /QUEUE/queueName
            //----------------------------------------------------
            ensureExists(this.queuePath);

            String node = zk.create(queuePath + "/", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            logger.info("{} produce a message to queue：{}, message data is: {}", Thread.currentThread().getName(), node, new String(data, "UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void ensureExists(String path) {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                zk.create(path, ROOT_QUEUE_DATA, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
