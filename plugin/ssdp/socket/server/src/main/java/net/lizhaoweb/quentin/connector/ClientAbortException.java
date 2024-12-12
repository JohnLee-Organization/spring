/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin.connector
 * @date : 2024-06-11
 * @time : 09:52
 */
package net.lizhaoweb.quentin.connector;

import lombok.Getter;

import java.io.IOException;

/**
 * Wrap an IOException identifying it as being caused by an abort
 * of a request by a remote client.
 * <p>
 * Created by jhon on 2024/6/11 9:52
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public final class ClientAbortException extends IOException {


    //------------------------------------------------------------ Constructors

    /**
     * Construct a new ClientAbortException with no other information.
     */
    public ClientAbortException() {
        this(null, null);
    }


    /**
     * Construct a new ClientAbortException for the specified message.
     *
     * @param message Message describing this exception
     */
    public ClientAbortException(String message) {
        this(message, null);
    }


    /**
     * Construct a new ClientAbortException for the specified throwable.
     *
     * @param throwable Throwable that caused this exception
     */
    public ClientAbortException(Throwable throwable) {
        this(null, throwable);
    }


    /**
     * Construct a new ClientAbortException for the specified message
     * and throwable.
     *
     * @param message   Message describing this exception
     * @param throwable Throwable that caused this exception
     */
    public ClientAbortException(String message, Throwable throwable) {
        super();
        this.message = message;
        this.throwable = throwable;
    }


    //------------------------------------------------------ Instance Variables


    /**
     * The error message passed to our constructor (if any)
     */
    @Getter
    private String message = null;


    /**
     * The underlying exception or error passed to our constructor (if any)
     */
    private Throwable throwable = null;


    //---------------------------------------------------------- Public Methods


    /**
     * Returns the cause that caused this exception, if any.
     */
    @Override
    public Throwable getCause() {
        return (throwable);
    }


    /**
     * Return a formatted string that describes this exception.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ClientAbortException:  ");
        if (message != null) {
            sb.append(message);
            if (throwable != null) {
                sb.append(":  ");
            }
        }
        if (throwable != null) {
            sb.append(throwable);
        }
        return (sb.toString());
    }

}
