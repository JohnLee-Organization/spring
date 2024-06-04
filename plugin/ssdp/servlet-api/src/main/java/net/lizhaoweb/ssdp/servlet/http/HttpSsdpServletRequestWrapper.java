/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet.http
 * @date : 2024-06-04
 * @time : 12:56
 */
package net.lizhaoweb.ssdp.servlet.http;

import net.lizhaoweb.ssdp.servlet.SsdpServletRequestWrapper;

import java.util.Enumeration;

/**
 * Provides a convenient implementation of the HttpSsdpServletRequest interface that
 * can be subclassed by developers wishing to adapt the request to a Servlet.
 * This class implements the Wrapper or Decorator pattern. Methods default to
 * calling through to the wrapped request object.
 * <p>
 * Created by jhon on 2024/6/4 12:56
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see net.lizhaoweb.ssdp.servlet.http.HttpSsdpServletRequest
 */
public class HttpSsdpServletRequestWrapper extends SsdpServletRequestWrapper implements HttpSsdpServletRequest {


    /**
     * Constructs a request object wrapping the given request.
     *
     * @throws java.lang.IllegalArgumentException if the request is null
     */
    public HttpSsdpServletRequestWrapper(HttpSsdpServletRequest request) {
        super(request);
    }

    private HttpSsdpServletRequest _getHttpServletRequest() {
        return (HttpSsdpServletRequest) super.getRequest();
    }

    /**
     * The default behavior of this method is to return getDateHeader(String name)
     * on the wrapped request object.
     */
    @Override
    public long getDateHeader(String name) {
        return this._getHttpServletRequest().getDateHeader(name);
    }

    /**
     * The default behavior of this method is to return getHeader(String name)
     * on the wrapped request object.
     */
    @Override
    public String getHeader(String name) {
        return this._getHttpServletRequest().getHeader(name);
    }

    /**
     * The default behavior of this method is to return getHeaders(String name)
     * on the wrapped request object.
     */
    @Override
    public Enumeration<String[]> getHeaders(String name) {
        return this._getHttpServletRequest().getHeaders(name);
    }

    /**
     * The default behavior of this method is to return getHeaderNames()
     * on the wrapped request object.
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return this._getHttpServletRequest().getHeaderNames();
    }

    /**
     * The default behavior of this method is to return getIntHeader(String name)
     * on the wrapped request object.
     */
    @Override
    public int getIntHeader(String name) {
        return this._getHttpServletRequest().getIntHeader(name);
    }

    /**
     * The default behavior of this method is to return getMethod()
     * on the wrapped request object.
     */
    @Override
    public String getMethod() {
        return this._getHttpServletRequest().getMethod();
    }

    /**
     * The default behavior of this method is to return getPathInfo()
     * on the wrapped request object.
     */
    @Override
    public String getPathInfo() {
        return this._getHttpServletRequest().getPathInfo();
    }

    /**
     * The default behavior of this method is to return getPathTranslated()
     * on the wrapped request object.
     */
    @Override
    public String getPathTranslated() {
        return this._getHttpServletRequest().getPathTranslated();
    }

    /**
     * The default behavior of this method is to return getContextPath()
     * on the wrapped request object.
     */
    @Override
    public String getContextPath() {
        return this._getHttpServletRequest().getContextPath();
    }

    /**
     * The default behavior of this method is to return getQueryString()
     * on the wrapped request object.
     */
    @Override
    public String getQueryString() {
        return this._getHttpServletRequest().getQueryString();
    }


    /**
     * The default behavior of this method is to return getRequestedSessionId()
     * on the wrapped request object.
     */
    @Override
    public String getRequestedSessionId() {
        return this._getHttpServletRequest().getRequestedSessionId();
    }

    /**
     * The default behavior of this method is to return getRequestURI()
     * on the wrapped request object.
     */
    @Override
    public String getRequestURI() {
        return this._getHttpServletRequest().getRequestURI();
    }

    /**
     * The default behavior of this method is to return getRequestURL()
     * on the wrapped request object.
     */
    @Override
    public StringBuffer getRequestURL() {
        return this._getHttpServletRequest().getRequestURL();
    }


    /**
     * The default behavior of this method is to return getServletPath()
     * on the wrapped request object.
     */
    @Override
    public String getServletPath() {
        return this._getHttpServletRequest().getServletPath();
    }


    /**
     * The default behavior of this method is to return getSession(boolean create)
     * on the wrapped request object.
     */
    @Override
    public HttpSsdpSession getSession(boolean create) {
        return this._getHttpServletRequest().getSession(create);
    }

    /**
     * The default behavior of this method is to return getSession()
     * on the wrapped request object.
     */
    @Override
    public HttpSsdpSession getSession() {
        return this._getHttpServletRequest().getSession();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdValid()
     * on the wrapped request object.
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return this._getHttpServletRequest().isRequestedSessionIdValid();
    }

    /**
     * The default behavior of this method is to return isRequestedSessionIdFromURL()
     * on the wrapped request object.
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return this._getHttpServletRequest().isRequestedSessionIdFromURL();
    }

}
