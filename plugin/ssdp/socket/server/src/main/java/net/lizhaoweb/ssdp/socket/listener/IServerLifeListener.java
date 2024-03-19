/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.listener
 * @date : 2024-03-14
 * @time : 10:44
 */
package net.lizhaoweb.ssdp.socket.listener;

/**
 * [接口] 服务器监听器
 * <p>
 * Created by Jhon.Lee on 2024/3/14 10:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.1.0.0.1
 * @email 404644381@qq.com
 */
public interface IServerLifeListener extends IEventListener {

    void onPre(IServerEvent event);

    void onExe(IServerEvent event);

    void onPos(IServerEvent event);
}
