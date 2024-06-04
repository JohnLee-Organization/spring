/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 10:54
 */
package net.lizhaoweb.ssdp.servlet;

/**
 * Defines a general exception a servlet can throw when it
 * encounters difficulty.
 * <p>
 * Created by jhon on 2024/6/4 10:54
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class SsdpServletException extends Exception {

    private Throwable rootCause;


    /**
     * Constructs a new servlet exception.
     */
    public SsdpServletException() {
        super();
    }


    /**
     * Constructs a new servlet exception with the
     * specified message. The message can be written
     * to the server log and/or displayed for the user.
     *
     * @param message a <code>String</code>
     *                specifying the text of
     *                the exception message
     */
    public SsdpServletException(String message) {
        super(message);
    }


    /**
     * Constructs a new servlet exception when the servlet
     * needs to throw an exception and include a message
     * about the "root cause" exception that interfered with its
     * normal operation, including a description message.
     *
     * @param message   a <code>String</code> containing
     *                  the text of the exception message
     * @param rootCause the <code>Throwable</code> exception
     *                  that interfered with the servlet's
     *                  normal operation, making this servlet
     *                  exception necessary
     */
    public SsdpServletException(String message, Throwable rootCause) {
        super(message, rootCause);
        this.rootCause = rootCause;
    }


    /**
     * Constructs a new servlet exception when the servlet
     * needs to throw an exception and include a message
     * about the "root cause" exception that interfered with its
     * normal operation.  The exception's message is based on the localized
     * message of the underlying exception.
     *
     * <p>This method calls the <code>getLocalizedMessage</code> method
     * on the <code>Throwable</code> exception to get a localized exception
     * message. When subclassing <code>ServletException</code>,
     * this method can be overridden to create an exception message
     * designed for a specific locale.
     *
     * @param rootCause the <code>Throwable</code> exception
     *                  that interfered with the servlet's
     *                  normal operation, making the servlet exception
     *                  necessary
     */
    public SsdpServletException(Throwable rootCause) {
        super(rootCause);
        this.rootCause = rootCause;
    }


    /**
     * Returns the exception that caused this servlet exception.
     *
     * @return the <code>Throwable</code>
     * that caused this servlet exception
     */
    public Throwable getRootCause() {
        return rootCause;
    }

}
