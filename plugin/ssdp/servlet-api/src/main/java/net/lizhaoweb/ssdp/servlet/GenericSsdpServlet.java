/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 12:07
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Defines a generic, protocol-independent
 * servlet. To write an HTTP servlet for use on the
 * Web, extend {@link net.lizhaoweb.ssdp.servlet.http.HttpSsdpServlet} instead.
 *
 * <p><code>GenericServlet</code> implements the <code>Servlet</code>
 * and <code>SsdpServletConfig</code> interfaces. <code>GenericServlet</code>
 * may be directly extended by a servlet, although it's more common to extend
 * a protocol-specific subclass such as <code>HttpServlet</code>.
 *
 * <p><code>GenericServlet</code> makes writing servlets
 * easier. It provides simple versions of the lifecycle methods
 * <code>init</code> and <code>destroy</code> and of the methods
 * in the <code>SsdpServletConfig</code> interface. <code>GenericServlet</code>
 * also implements the <code>log</code> method, declared in the
 * <code>SsdpServletContext</code> interface.
 *
 * <p>To write a generic servlet, you need only
 * override the abstract <code>service</code> method.
 * <p>
 * Created by jhon on 2024/6/4 12:07
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public abstract class GenericSsdpServlet implements SsdpServlet, SsdpServletConfig, Serializable {
    private static final String LSTRING_FILE = "net.lizhaoweb.ssdp.servlet.LocalStrings";
    private static ResourceBundle lStrings = ResourceBundle.getBundle(LSTRING_FILE);

    private transient SsdpServletConfig config;


    /**
     * Does nothing. All of the servlet initialization
     * is done by one of the <code>init</code> methods.
     */
    public GenericSsdpServlet() {
    }


    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being taken out of service.  See {@link SsdpServlet#destroy}.
     */
    @Override
    public void destroy() {
    }


    /**
     * Returns a <code>String</code> containing the value of the named
     * initialization parameter, or <code>null</code> if the parameter does
     * not exist.  See {@link SsdpServletConfig#getInitParameter}.
     *
     * <p>This method is supplied for convenience. It gets the
     * value of the named parameter from the servlet's
     * <code>SsdpServletConfig</code> object.
     *
     * @param name a <code>String</code> specifying the name
     *             of the initialization parameter
     * @return String        a <code>String</code> containing the value
     * of the initialization parameter
     */
    @Override
    public String getInitParameter(String name) {
        SsdpServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
        }
        return sc.getInitParameter(name);
    }


    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has no
     * initialization parameters.  See {@link
     * SsdpServletConfig#getInitParameterNames}.
     *
     * <p>This method is supplied for convenience. It gets the
     * parameter names from the servlet's <code>SsdpServletConfig</code> object.
     *
     * @return Enumeration    an enumeration of <code>String</code>
     * objects containing the names of
     * the servlet's initialization parameters
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        SsdpServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
        }
        return sc.getInitParameterNames();
    }


    /**
     * Returns this servlet's {@link SsdpServletConfig} object.
     *
     * @return SsdpServletConfig    the <code>SsdpServletConfig</code> object
     * that initialized this servlet
     */
    @Override
    public SsdpServletConfig getServletConfig() {
        return config;
    }


    /**
     * Returns a reference to the {@link SsdpServletContext} in which this servlet
     * is running.  See {@link SsdpServletConfig#getServletContext}.
     *
     * <p>This method is supplied for convenience. It gets the
     * context from the servlet's <code>SsdpServletConfig</code> object.
     *
     * @return SsdpServletContext    the <code>SsdpServletContext</code> object
     * passed to this servlet by the <code>init</code>
     * method
     */
    @Override
    public SsdpServletContext getServletContext() {
        SsdpServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
        }
        return sc.getServletContext();
    }


    /**
     * Returns information about the servlet, such as
     * author, version, and copyright.
     * By default, this method returns an empty string.  Override this method
     * to have it return a meaningful value.  See {@link
     * SsdpServlet#getServletInfo}.
     *
     * @return String        information about this servlet, by default an
     * empty string
     */
    @Override
    public String getServletInfo() {
        return "";
    }


    /**
     * Called by the servlet container to indicate to a servlet that the
     * servlet is being placed into service.  See {@link SsdpServlet#init}.
     *
     * <p>This implementation stores the {@link SsdpServletConfig}
     * object it receives from the servlet container for later use.
     * When overriding this form of the method, call
     * <code>super.init(config)</code>.
     *
     * @param config the <code>SsdpServletConfig</code> object
     *               that contains configutation
     *               information for this servlet
     * @throws SsdpServletException if an exception occurs that
     *                              interrupts the servlet's normal
     *                              operation
     */
    @Override
    public void init(SsdpServletConfig config) throws SsdpServletException {
        this.config = config;
        this.init();
    }


    /**
     * A convenience method which can be overridden so that there's no need
     * to call <code>super.init(config)</code>.
     *
     * <p>Instead of overriding {@link #init(SsdpServletConfig)}, simply override
     * this method and it will be called by
     * <code>GenericServlet.init(SsdpServletConfig config)</code>.
     * The <code>SsdpServletConfig</code> object can still be retrieved via {@link
     * #getServletConfig}.
     *
     * @throws SsdpServletException if an exception occurs that
     *                              interrupts the servlet's
     *                              normal operation
     */
    public void init() throws SsdpServletException {
    }


    /**
     * Writes the specified message to a servlet log file, prepended by the
     * servlet's name.  See {@link SsdpServletContext#log(String)}.
     *
     * @param msg a <code>String</code> specifying
     *            the message to be written to the log file
     */
    public void log(String msg) {
        getServletContext().log(getServletName() + ": " + msg);
    }


    /**
     * Writes an explanatory message and a stack trace
     * for a given <code>Throwable</code> exception
     * to the servlet log file, prepended by the servlet's name.
     * See {@link SsdpServletContext#log(String, Throwable)}.
     *
     * @param message a <code>String</code> that describes
     *                the error or exception
     * @param t       the <code>java.lang.Throwable</code> error
     *                or exception
     */
    public void log(String message, Throwable t) {
        getServletContext().log(getServletName() + ": " + message, t);
    }


    /**
     * Called by the servlet container to allow the servlet to respond to
     * a request.  See {@link SsdpServlet#service}.
     *
     * <p>This method is declared abstract so subclasses, such as
     * <code>HttpServlet</code>, must override it.
     *
     * @param req the <code>SsdpServletRequest</code> object
     *            that contains the client's request
     * @param res the <code>SsdpServletResponse</code> object
     *            that will contain the servlet's response
     * @throws SsdpServletException if an exception occurs that
     *                              interferes with the servlet's
     *                              normal operation occurred
     * @throws IOException          if an input or output
     *                              exception occurs
     */
    @Override
    public abstract void service(SsdpServletRequest req, SsdpServletResponse res) throws SsdpServletException, IOException;


    /**
     * Returns the name of this servlet instance.
     * See {@link SsdpServletConfig#getServletName}.
     *
     * @return the name of this servlet instance
     */
    @Override
    public String getServletName() {
        SsdpServletConfig sc = getServletConfig();
        if (sc == null) {
            throw new IllegalStateException(lStrings.getString("err.servlet_config_not_initialized"));
        }
        return sc.getServletName();
    }

}
