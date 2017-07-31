/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 12:12
 */
package net.lizhaoweb.spring.zk.queue;

import org.apache.commons.lang3.RandomUtils;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
class ProduceThread extends Thread {

    private IZooKeeperQueue queue;

    public ProduceThread(IZooKeeperQueue queue, String name) {
        super(name);
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(RandomUtils.nextInt(100 * i, 200 * i));
                queue.produce(("massive" + i).getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
