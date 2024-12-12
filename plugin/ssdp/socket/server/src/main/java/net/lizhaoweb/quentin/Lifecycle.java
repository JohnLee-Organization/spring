/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-08
 * @time : 05:51
 */
package net.lizhaoweb.quentin;

/**
 * Common interface for component life cycle methods.  Catalina components
 * may implement this interface (as well as the appropriate interface(s) for
 * the functionality they support) in order to provide a consistent mechanism
 * to start and stop the component.
 * <br>
 * The valid state transitions for components that support Lifecycle are:
 * <pre>
 *                                  --------------------<-----------------------
 *                                  |                                          |
 *    init()           start()      |        auto          auto         stop() |
 * NEW ->-- INITIALIZED -->-- STARTING_PREP -->- STARTING -->- STARTED -->---  |
 * |||                              ^                             |         |  |
 * |||        start()               |                             |         |  |
 * ||----------->--------------------                             |         |  |
 * ||                                                             |         |  |
 * |---                auto                    auto               |         |  |
 * |  |          ---------<----- MUST_STOP ---------------------<--         |  |
 * |  |          |                                                          |  |
 * |  |          ---------------------------<--------------------------------  ^
 * |  |          |                                                             |
 * |  |          |             auto                 auto              start()  |
 * |  |     STOPPING_PREP ------>----- STOPPING ------>----- STOPPED ---->------
 * |  |          ^                                           |  |  ^
 * |  |          |                                  auto     |  |  |
 * |  |          |stop()            MUST_DESTROY------<-------  |  |
 * |  |          |                    |                         |  |
 * |  |          |                    |auto                     |  |
 * |  |          |    destroy()      \|/              destroy() |  |
 * |  |       FAILED ---->------ DESTROYED ----<-----------------  |
 * |  |                             ^                              |
 * |  |        destroy()            |                              |
 * |  -------------------------------                              |
 * |                                                               |
 * |                            stop()                             |
 * --->------------------------------>------------------------------
 *
 * Any state can transition to FAILED.
 *
 * Calling start() while a component is in states STARTING_PREP, STARTING or
 * STARTED has no effect.
 *
 * Calling start() while a component is in state NEW will cause init() to be
 * called immediately after the start() method is entered.
 *
 * Calling stop() while a component is in states STOPPING_PREP, STOPPING or
 * STOPPED has no effect.
 *
 * Calling stop() while a component is in state NEW transitions the component
 * to STOPPED. This is typically encountered when a component fails to start and
 * does not start all its sub-components. When the component is stopped, it will
 * try to stop all sub-components - even those it didn't start.
 *
 * MUST_STOP is used to indicate that the {@link #stop()} should be called on
 * the component as soon as {@link #start()} exits. It is typically used when a
 * component has failed to start.
 *
 * MUST_DESTROY is used to indicate that the {@link #stop()} should be called on
 * the component as soon as {@link #stop()} exits. It is typically used when a
 * component is not designed to be restarted.
 *
 * Attempting any other transition will throw {@link LifecycleException}.
 *
 * </pre>
 * The {@link LifecycleEvent}s fired during state changes are defined in the
 * methods that trigger the changed. No {@link LifecycleEvent}s are fired if the
 * attempted transition is not valid.
 * <p>
 * TODO: Not all components may transition from STOPPED to STARTING_PREP
 * <p>
 * Created by jhon on 2024/6/8 5:51
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface Lifecycle {


    // ----------------------------------------------------- Manifest Constants


    /**
     * The LifecycleEvent type for the "component init" event.
     */
    public static final String INIT_EVENT = "init";


    /**
     * The LifecycleEvent type for the "component start" event.
     */
    public static final String START_EVENT = "start";


    /**
     * The LifecycleEvent type for the "component before start" event.
     */
    public static final String BEFORE_START_EVENT = "before_start";


    /**
     * The LifecycleEvent type for the "component after start" event.
     */
    public static final String AFTER_START_EVENT = "after_start";


    /**
     * The LifecycleEvent type for the "component stop" event.
     */
    public static final String STOP_EVENT = "stop";


    /**
     * The LifecycleEvent type for the "component before stop" event.
     */
    public static final String BEFORE_STOP_EVENT = "before_stop";


    /**
     * The LifecycleEvent type for the "component after stop" event.
     */
    public static final String AFTER_STOP_EVENT = "after_stop";


    /**
     * The LifecycleEvent type for the "component destroy" event.
     */
    public static final String DESTROY_EVENT = "destroy";


    /**
     * The LifecycleEvent type for the "periodic" event.
     */
    public static final String PERIODIC_EVENT = "periodic";


    /**
     * The LifecycleEvent type for the "configure_start" event. Used by those
     * components that use a separate component to perform configuration and
     * need to signal when configuration should be performed - usually after
     * {@link #BEFORE_START_EVENT} and before {@link #START_EVENT}.
     */
    public static final String CONFIGURE_START_EVENT = "configure_start";


    /**
     * The LifecycleEvent type for the "configure_stop" event. Used by those
     * components that use a separate component to perform configuration and
     * need to signal when de-configuration should be performed - usually after
     * {@link #STOP_EVENT} and before {@link #AFTER_STOP_EVENT}.
     */
    public static final String CONFIGURE_STOP_EVENT = "configure_stop";


    // --------------------------------------------------------- Public Methods


    /**
     * Add a LifecycleEvent listener to this component.
     *
     * @param listener The listener to add
     */
    void addLifecycleListener(LifecycleListener listener);


    /**
     * Get the lifecycle listeners associated with this lifecycle. If this
     * Lifecycle has no listeners registered, a zero-length array is returned.
     */
    LifecycleListener[] findLifecycleListeners();


    /**
     * Remove a LifecycleEvent listener from this component.
     *
     * @param listener The listener to remove
     */
    void removeLifecycleListener(LifecycleListener listener);


    /**
     * Prepare the component for starting. This method should perform any
     * initialization required post object creation. The following
     * {@link LifecycleEvent}s will be fired in the following order:
     * <ol>
     *   <li>INIT_EVENT: On the successful completion of component
     *                   initialization.</li>
     * </ol>
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that prevents this component from being used
     */
    void init() throws LifecycleException;

    /**
     * Prepare for the beginning of active use of the public methods of this
     * component.  This method should be called before any of the public
     * methods of this component are utilized. The following
     * {@link LifecycleEvent}s will be fired in the following order:
     * <ol>
     *   <li>BEFORE_START_EVENT: At the beginning of the method. It is as this
     *                           point the state transitions to
     *                           {@link LifecycleState#STARTING_PREP}.</li>
     *   <li>START_EVENT: During the method once it is safe to call start() for
     *                    any child components. It is at this point that the
     *                    state transitions to {@link LifecycleState#STARTING}
     *                    and that the public methods may be used.</li>
     *   <li>AFTER_START_EVENT: At the end of the method, immediately before it
     *                          returns. It is at this point that the state
     *                          transitions to {@link LifecycleState#STARTED}.
     *                          </li>
     * </ol>
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that prevents this component from being used
     */
    void start() throws LifecycleException;


    /**
     * Gracefully terminate the active use of the public methods of this
     * component. Once the STOP_EVENT is fired, the public methods should not
     * be used. The following {@link LifecycleEvent}s will be fired in the
     * following order:
     * <ol>
     *   <li>BEFORE_STOP_EVENT: At the beginning of the method. It is at this
     *                          point that the state transitions to
     *                          {@link LifecycleState#STOPPING_PREP}.</li>
     *   <li>STOP_EVENT: During the method once it is safe to call stop() for
     *                   any child components. It is at this point that the
     *                   state transitions to {@link LifecycleState#STOPPING}
     *                   and that the public methods may no longer be used.</li>
     *   <li>AFTER_STOP_EVENT: At the end of the method, immediately before it
     *                         returns. It is at this point that the state
     *                         transitions to {@link LifecycleState#STOPPED}.
     *                         </li>
     * </ol>
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that needs to be reported
     */
    void stop() throws LifecycleException;

    /**
     * Prepare to discard the object. The following {@link LifecycleEvent}s will
     * be fired in the following order:
     * <ol>
     *   <li>DESTROY_EVENT: On the successful completion of component
     *                      destruction.</li>
     * </ol>
     *
     * @throws LifecycleException if this component detects a fatal error
     *                            that prevents this component from being used
     */
    void destroy() throws LifecycleException;


    /**
     * Obtain the current state of the source component.
     *
     * @return The current state of the source component.
     */
    LifecycleState getState();

}
