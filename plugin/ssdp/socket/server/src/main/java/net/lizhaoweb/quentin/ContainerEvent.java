/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-11
 * @time : 08:40
 */
package net.lizhaoweb.quentin;

import lombok.Getter;

import java.util.EventObject;

/**
 * General event for notifying listeners of significant changes on a Container.
 * <p>
 * Created by jhon on 2024/6/11 8:40
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public final class ContainerEvent extends EventObject {


    /**
     * The Container on which this event occurred.
     */
    @Getter
    private Container container = null;


    /**
     * The event data associated with this event.
     */
    @Getter
    private Object data = null;


    /**
     * The event type this instance represents.
     */
    @Getter
    private String type = null;


    /**
     * Construct a new ContainerEvent with the specified parameters.
     *
     * @param container Container on which this event occurred
     * @param type      Event type
     * @param data      Event data
     */
    public ContainerEvent(Container container, String type, Object data) {
        super(container);
        this.container = container;
        this.type = type;
        this.data = data;
    }


    /**
     * Return a string representation of this event.
     */
    @Override
    public String toString() {
        return ("ContainerEvent['" + getContainer() + "','" + getType() + "','" + getData() + "']");
    }


}
