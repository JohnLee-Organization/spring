/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp
 * @date : 2024-03-07
 * @time : 11:38
 */
package net.lizhaoweb.ssdp;

import java.io.Closeable;

/**
 * [接口] SSDP服务器
 * <p>
 * Created by Jhon.Lee on 3/7/2024 11:38
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
public interface ISsdpServer extends Closeable {

    /**
     * 初始化
     */
    void init();

    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 销毁
     */
    void destroy();
}
