/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-05-31
 * @time : 16:32
 */
package net.lizhaoweb.ssdp.servlet.http;

import net.lizhaoweb.ssdp.servlet.*;

import java.io.IOException;

/**
 * Provides a base class that implements the Filter interface and ensures that the Request and Response are of type
 * HttpServletRequest and HttpServletResponse respectively.
 * <p>
 * Created by jhon on 2024/5/31 16:32
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public abstract class HttpFilter extends GenericFilter {

    /**
     * {@inheritDoc} This implementation tests the request and response to see if they are instances of
     * {@link HttpServletRequest} and {@link HttpSsdpServletResponse} respectively. If they are then they are passed to
     * {@link #doFilter(HttpServletRequest, HttpSsdpServletResponse, FilterChain)}. If not, a {@link SsdpServletException} is
     * thrown.
     *
     * @throws SsdpServletException If either the request or response are not of the expected types or any other error
     *                          occurs
     */
    @Override
    public void doFilter(ServletRequest request, SsdpServletResponse response, FilterChain chain) throws IOException, SsdpServletException {
        if (!(request instanceof HttpServletRequest)) {
            throw new SsdpServletException(request + " not HttpServletRequest");
        }
        if (!(response instanceof HttpSsdpServletResponse)) {
            throw new SsdpServletException(response + " not HttpServletResponse");
        }
        doFilter((HttpServletRequest) request, (HttpSsdpServletResponse) response, chain);
    }


    /**
     * The <code>doFilter</code> method of the Filter is called by the container each time a request/response pair is
     * passed through the chain due to a client request for a resource at the end of the chain. The FilterChain passed
     * in to this method allows the Filter to pass on the request and response to the next entity in the chain.
     * <p>
     * A typical implementation of this method would follow the following pattern:- <br>
     * 1. Examine the request<br>
     * 2. Optionally wrap the request object with a custom implementation to filter content or headers for input
     * filtering <br>
     * 3. Optionally wrap the response object with a custom implementation to filter content or headers for output
     * filtering <br>
     * 4. a) <strong>Either</strong> invoke the next entity in the chain using the FilterChain object
     * (<code>chain.doFilter()</code>), <br>
     * 4. b) <strong>or</strong> not pass on the request/response pair to the next entity in the filter chain to block
     * the request processing<br>
     * 5. Directly set headers on the response after invocation of the next entity in the filter chain. This default
     * implementation simply calls the next filter in the filter chain.
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException      if an I/O error occurs during this filter's processing of the request
     * @throws SsdpServletException if the processing fails for any other reason
     */
    protected void doFilter(HttpServletRequest request, HttpSsdpServletResponse response, FilterChain chain) throws IOException, SsdpServletException {
        chain.doFilter(request, response);
    }

}
