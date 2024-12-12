/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-11
 * @time : 08:39
 */
package net.lizhaoweb.quentin;

/**
 * Interface defining a listener for significant Container generated events.
 * Note that "container start" and "container stop" events are normally
 * LifecycleEvents, not ContainerEvents.
 * <p>
 * Created by jhon on 2024/6/11 8:39
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface ContainerListener {


    /**
     * Acknowledge the occurrence of the specified event.
     *
     * @param event ContainerEvent that has occurred
     */
    void containerEvent(ContainerEvent event);

}
