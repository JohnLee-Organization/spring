/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.socket.model
 * @date : 2024-05-16
 * @time : 09:57
 */
package net.lizhaoweb.ssdp.socket.model;

/**
 * [模型] 服务器状态
 * <p>
 * Created by jhon on 2024/5/16 9:57
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public enum ServerStatus {
    PRE_INSTANCE, // 开始实例化
    INSTANCING, // 正在实例化
    INSTANCED, // 已经实例化

    PRE_INITIALIZE, // 准备初始化
    INITIALIZING, // 正在初始化
    INITIALIZED, // 已经初始化

    PRE_START, // 准备启动
    STARTING, // 正在启动
    STARTED, // 已启动

    PRE_RUN, // 准备运行
    RUNNING, // 运行中

    PRE_STOP, // 准备停止
    STOPPING, // 正在停止
    STOPPED, // 已经停止

    PRE_CLOSE, // 准备关闭
    CLOSING, // 正在关闭
    CLOSED, // 已经关闭

    PRE_DESTROY, // 准备销毁
    DESTROYING, // 正在销毁
    DESTROYED, // 已经销毁
    ;

}
