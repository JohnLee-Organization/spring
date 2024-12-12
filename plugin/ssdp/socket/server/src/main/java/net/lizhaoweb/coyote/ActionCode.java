/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 11:23
 */
package net.lizhaoweb.coyote;

import lombok.Getter;

/**
 * Enumerated class containing the adapter event codes.
 * Actions represent callbacks from the servlet container to the coyote
 * connector.
 * <p>
 * Actions are implemented by ProtocolHandler, using the ActionHook interface.
 * <p>
 * Created by jhon on 2024/6/7 11:23
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ProtocolHandler
 * @see ActionHook
 */
@SuppressWarnings("unused")
public final class ActionCode {


    // -------------------------------------------------------------- Constants


    public static final ActionCode ACTION_ACK = new ActionCode(1);


    public static final ActionCode ACTION_CLOSE = new ActionCode(2);


    public static final ActionCode ACTION_COMMIT = new ActionCode(3);


    /**
     * A flush() operation originated by the client ( i.e. a flush() on
     * the servlet output stream or writer, called by a servlet ).
     * <p>
     * Argument is the Response.
     */
    public static final ActionCode ACTION_CLIENT_FLUSH = new ActionCode(4);


    public static final ActionCode ACTION_CUSTOM = new ActionCode(5);


    public static final ActionCode ACTION_RESET = new ActionCode(6);


    public static final ActionCode ACTION_START = new ActionCode(7);


    public static final ActionCode ACTION_STOP = new ActionCode(8);


    public static final ActionCode ACTION_WEBAPP = new ActionCode(9);

    /**
     * Hook called after request, but before recycling. Can be used
     * for logging, to update counters, custom cleanup - the request
     * is still visible
     */
    public static final ActionCode ACTION_POST_REQUEST = new ActionCode(10);

    /**
     * Callback for lazy evaluation - extract the remote host address.
     */
    public static final ActionCode ACTION_REQ_HOST_ATTRIBUTE = new ActionCode(11);


    /**
     * Callback for lazy evaluation - extract the remote host infos (address, name, port) and local address.
     */
    public static final ActionCode ACTION_REQ_HOST_ADDR_ATTRIBUTE = new ActionCode(12);


    /**
     * Chain for request creation. Called each time a new request is created
     * ( requests are recycled ).
     */
    public static final ActionCode ACTION_NEW_REQUEST = new ActionCode(14);


    /**
     * Callback for lazy evaluation - socket remote port.
     **/
    public static final ActionCode ACTION_REQ_REMOTEPORT_ATTRIBUTE = new ActionCode(16);


    /**
     * Callback for lazy evaluation - socket local port.
     **/
    public static final ActionCode ACTION_REQ_LOCALPORT_ATTRIBUTE = new ActionCode(17);


    /**
     * Callback for lazy evaluation - local address.
     **/
    public static final ActionCode ACTION_REQ_LOCAL_ADDR_ATTRIBUTE = new ActionCode(18);


    /**
     * Callback for lazy evaluation - local address.
     **/
    public static final ActionCode ACTION_REQ_LOCAL_NAME_ATTRIBUTE = new ActionCode(19);


    /**
     * Callback for setting FORM auth body replay
     */
    public static final ActionCode ACTION_REQ_SET_BODY_REPLAY = new ActionCode(20);


    /**
     * Callback for begin Comet processing
     */
    public static final ActionCode ACTION_COMET_BEGIN = new ActionCode(21);


    /**
     * Callback for end Comet processing
     */
    public static final ActionCode ACTION_COMET_END = new ActionCode(22);


    /**
     * Callback for getting the amount of available bytes
     */
    public static final ActionCode ACTION_AVAILABLE = new ActionCode(23);

    /**
     * Callback for an asynchronous close of the Comet event
     */
    public static final ActionCode ACTION_COMET_CLOSE = new ActionCode(24);

    /**
     * Callback for setting the timeout asynchronously
     */
    public static final ActionCode ACTION_COMET_SETTIMEOUT = new ActionCode(25);

    /**
     * Callback for an async request
     */
    public static final ActionCode ACTION_ASYNC_START = new ActionCode(26);

    /**
     * Callback for an async call to {@link javax.servlet.AsyncContext#complete()}
     */
    public static final ActionCode ACTION_ASYNC_COMPLETE = new ActionCode(27);
    /**
     * Callback for an async call to {@link javax.servlet.ServletRequest#setAsyncTimeout(long)}
     */
    public static final ActionCode ACTION_ASYNC_SETTIMEOUT = new ActionCode(28);

    /**
     * Callback for an async call to {@link javax.servlet.AsyncContext#dispatch()}
     */
    public static final ActionCode ACTION_ASYNC_DISPATCH = new ActionCode(29);


    // ----------------------------------------------------------- Constructors
    @Getter
    int code;

    /**
     * Private constructor.
     */
    private ActionCode(int code) {
        this.code = code;
    }

}
