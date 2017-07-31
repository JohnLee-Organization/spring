/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.zk.queue
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:27
 */
package net.lizhaoweb.spring.zk.queue;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2017年07月18日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class ConsumeThread extends Thread {

    private IZooKeeperQueue queue;

    public ConsumeThread(IZooKeeperQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            queue.consume();
            System.out.println("--------------------------------------------------------\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
