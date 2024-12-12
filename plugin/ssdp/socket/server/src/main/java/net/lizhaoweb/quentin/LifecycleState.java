/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-08
 * @time : 06:01
 */
package net.lizhaoweb.quentin;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The list of valid states for components that implement {@link Lifecycle}.
 * See {@link Lifecycle} for the state transition diagram.
 * <p>
 * Created by jhon on 2024/6/8 6:01
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum LifecycleState {
    NEW(false, null), //
    INITIALIZED(false, Lifecycle.INIT_EVENT), //
    STARTING_PREP(false, Lifecycle.BEFORE_START_EVENT), //
    STARTING(true, Lifecycle.START_EVENT), //
    STARTED(true, Lifecycle.AFTER_START_EVENT), //
    STOPPING_PREP(true, Lifecycle.BEFORE_STOP_EVENT), //
    STOPPING(false, Lifecycle.STOP_EVENT), //
    STOPPED(false, Lifecycle.AFTER_STOP_EVENT), //
    DESTROYED(false, Lifecycle.DESTROY_EVENT), //
    FAILED(false, null), //
    MUST_STOP(true, null), //
    MUST_DESTROY(true, null) //
    ;

    /**
     * Is a component in this state available for use?
     */
    @Getter
    private final boolean available;
    @Getter
    private final String lifecycleEvent;

}
