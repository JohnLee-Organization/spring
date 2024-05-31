/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:11
 */
package net.lizhaoweb.ssdp.servlet;

import java.util.Enumeration;

/**
 * A filter configuration object used by a servlet container
 * to pass information to a filter during initialization.
 * <p>
 * Created by jhon on 2024/5/30 15:11
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see Filter
 */
@SuppressWarnings("unused")
public interface FilterConfig {

    /**
     * Returns the filter-name of this filter as defined in the deployment descriptor.
     */
    String getFilterName();


    /**
     * Returns a reference to the {@link ServletContext} in which the caller
     * is executing.
     *
     * @return a {@link ServletContext} object, used
     * by the caller to interact with its servlet
     * container
     * @see ServletContext
     */
    ServletContext getServletContext();

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
     * Returns the names of the filter's initialization parameters
     * as an <code>Enumeration</code> of <code>String</code> objects,
     * or an empty <code>Enumeration</code> if the filter has
     * no initialization parameters.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the filter's
     * initialization parameters
     */
    Enumeration<String> getInitParameterNames();

}
