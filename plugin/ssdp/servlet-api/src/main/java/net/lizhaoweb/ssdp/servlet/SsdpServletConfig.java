/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 11:31
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.Enumeration;

/**
 * A servlet configuration object used by a servlet container
 * to pass information to a servlet during initialization.
 * <p>
 * Created by jhon on 2024/6/4 11:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface SsdpServletConfig {


    /**
     * Returns the name of this servlet instance.
     * The name may be provided via server administration, assigned in the
     * web application deployment descriptor, or for an unregistered (and thus
     * unnamed) servlet instance it will be the servlet's class name.
     *
     * @return the name of the servlet instance
     */
    String getServletName();

    /**
     * Returns a reference to the {@link SsdpServletContext} in which the caller
     * is executing.
     *
     * @return a {@link SsdpServletContext} object, used
     * by the caller to interact with its servlet
     * container
     * @see SsdpServletContext
     */
    SsdpServletContext getServletContext();

    /**
     * Returns a <code>String</code> containing the value of the
     * named initialization parameter, or <code>null</code> if
     * the parameter does not exist.
     *
     * @param name a <code>String</code> specifying the name
     *             of the initialization parameter
     * @return a <code>String</code> containing the value
     * of the initialization parameter
     */
    String getInitParameter(String name);


    /**
     * Returns the names of the servlet's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the servlet has
     * no initialization parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the servlet's
     * initialization parameters
     */
    Enumeration<String> getInitParameterNames();

}
