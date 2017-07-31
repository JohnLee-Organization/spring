/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 10:51
 */
package net.lizhaoweb.spring.zk.queue;

import net.lizhaoweb.spring.zk.common.IZooKeeperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath*:/schema/spring/jl-spring-zk-client-model.xml"
})
public class TestZooKeeperQueue {

    @Resource(name = "net.lizhaoweb.spring.zk.common.ZooKeeperClient")
    private IZooKeeperClient zooKeeperClient;

    @Test
    public void test() {

        String queueName = "test";
        final IZooKeeperQueue queue = new ZooKeeperQueue(zooKeeperClient, queueName);

//        for (int i = 0; i < 10; i++) {
//            ConsumeThread consumeThread = new ConsumeThread(queue);
//            consumeThread.start();
//        }

        ProduceThread produceThread = new ProduceThread(queue, "Produce-thread");
        produceThread.start();

    }
}
