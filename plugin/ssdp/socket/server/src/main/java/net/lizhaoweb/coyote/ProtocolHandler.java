/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-06
 * @time : 17:12
 */
package net.lizhaoweb.coyote;

import java.util.Iterator;

/**
 * Abstract the protocol implementation, including threading, etc.
 * Processor is single threaded and specific to stream-based protocols,
 * will not fit Jk protocols like JNI.
 * <p>
 * This is the main interface to be implemented by a coyote connector.
 * Adapter is the main interface to be implemented by a coyote servlet container.
 * <p>
 * Created by jhon on 2024/6/6 17:12
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see Adapter
 */
@SuppressWarnings("unused")
public interface ProtocolHandler {


    /**
     * Pass config info.
     */
    void setAttribute(String name, Object value);


    Object getAttribute(String name);

    Iterator<String> getAttributeNames();

    /**
     * The adapter, used to call the connector.
     */
    void setAdapter(Adapter adapter);


    Adapter getAdapter();


    /**
     * Init the protocol.
     */
    void init() throws Exception;


    /**
     * Start the protocol.
     */
    void start() throws Exception;

    /**
     * Pause the protocol (optional).
     */
    void pause() throws Exception;

    /**
     * Resume the protocol (optional).
     */
    void resume() throws Exception;

    void destroy() throws Exception;

}
