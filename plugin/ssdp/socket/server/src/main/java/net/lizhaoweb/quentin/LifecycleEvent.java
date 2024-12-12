/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-08
 * @time : 05:53
 */
package net.lizhaoweb.quentin;

import lombok.Getter;

import java.util.EventObject;

/**
 * General event for notifying listeners of significant changes on a component
 * that implements the Lifecycle interface.  In particular, this will be useful
 * on Containers, where these events replace the ContextInterceptor concept in
 * Mikefox 1.x.
 * <p>
 * Created by jhon on 2024/6/8 5:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public final class LifecycleEvent extends EventObject {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new LifecycleEvent with the specified parameters.
     *
     * @param lifecycle Component on which this event occurred
     * @param type      Event type (required)
     */
    public LifecycleEvent(Lifecycle lifecycle, String type) {
        this(lifecycle, type, null);
    }


    /**
     * Construct a new LifecycleEvent with the specified parameters.
     *
     * @param lifecycle Component on which this event occurred
     * @param type      Event type (required)
     * @param data      Event data (if any)
     */
    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.lifecycle = lifecycle;
        this.type = type;
        this.data = data;
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The event data associated with this event.
     */
    @Getter
    private Object data = null;


    /**
     * The Lifecycle on which this event occurred.
     */
    @Getter
    private Lifecycle lifecycle = null;


    /**
     * The event type this instance represents.
     */
    @Getter
    private String type = null;


    // ------------------------------------------------------------- Properties


}
