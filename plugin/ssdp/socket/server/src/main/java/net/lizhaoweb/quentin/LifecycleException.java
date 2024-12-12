/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin
 * @date : 2024-06-08
 * @time : 05:57
 */
package net.lizhaoweb.quentin;

import lombok.Getter;

/**
 * General purpose exception that is thrown to indicate a lifecycle related
 * problem.  Such exceptions should generally be considered fatal to the
 * operation of the application containing this component.
 * <p>
 * Created by jhon on 2024/6/8 5:57
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public final class LifecycleException extends Exception {


    //------------------------------------------------------------ Constructors


    /**
     * Construct a new LifecycleException with no other information.
     */
    public LifecycleException() {
        this(null, null);
    }


    /**
     * Construct a new LifecycleException for the specified message.
     *
     * @param message Message describing this exception
     */
    public LifecycleException(String message) {
        this(message, null);
    }


    /**
     * Construct a new LifecycleException for the specified throwable.
     *
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(Throwable throwable) {
        this(null, throwable);
    }


    /**
     * Construct a new LifecycleException for the specified message
     * and throwable.
     *
     * @param message   Message describing this exception
     * @param throwable Throwable that caused this exception
     */
    public LifecycleException(String message, Throwable throwable) {
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
    @Getter
    private Throwable throwable = null;


    //---------------------------------------------------------- Public Methods


    /**
     * Return a formatted string that describes this exception.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LifecycleException:  ");
        if (message != null) {
            sb.append(message);
            if (throwable != null) {
                sb.append(":  ");
            }
        }
        if (throwable != null) {
            sb.append(throwable.toString());
        }
        return (sb.toString());
    }

}
