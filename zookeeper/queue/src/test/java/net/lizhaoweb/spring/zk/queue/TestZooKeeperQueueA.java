/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 13:46
 */
package net.lizhaoweb.spring.zk.queue;

import org.apache.commons.lang3.RandomUtils;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({
//        "classpath*:/schema/spring/jl-spring-zk-client-model.xml"
//})
public class TestZooKeeperQueueA {

//    @Resource(name = "net.lizhaoweb.spring.zk.common.ZooKeeperClient")
//    private IZooKeeperClient zooKeeperClient;
//
//    @Test
//    public void main()  {
//
//        String queueName = "test";
//        final ZooKeeperQueueA queue = new ZooKeeperQueueA(queueName);
//
//        for (int i = 0; i < 10; i++) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        queue.consume();
//                        System.out.println("--------------------------------------------------------");
//                        System.out.println();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (KeeperException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    try {
//                        Thread.sleep(RandomUtils.nextInt(100 * i, 200 * i));
//                        queue.produce(("massive" + i).getBytes());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (KeeperException e) {
//                        e.printStackTrace();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, "Produce-thread").start();
//    }

    @Test
    public void main() throws IOException, InterruptedException, KeeperException {

        String queueName = "test";
        final ZooKeeperQueueA queue = new ZooKeeperQueueA(queueName);

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.consume();
                        System.out.println("--------------------------------------------------------");
                        System.out.println();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(RandomUtils.nextInt(100 * i, 200 * i));
                        queue.produce(("massive" + i).getBytes());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Produce-thread").start();


    }
}
