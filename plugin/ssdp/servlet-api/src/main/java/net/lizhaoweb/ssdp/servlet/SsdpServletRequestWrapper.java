/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 11:09
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Provides a convenient implementation of the ServletRequest interface that
 * can be subclassed by developers wishing to adapt the request to a Servlet.
 * This class implements the Wrapper or Decorator pattern. Methods default to
 * calling through to the wrapped request object.
 * <p>
 * Created by jhon on 2024/6/4 11:09
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class SsdpServletRequestWrapper implements SsdpServletRequest {

    private SsdpServletRequest request;

    /**
     * Creates a ServletRequest adaptor wrapping the given request object.
     *
     * @param request The request
     * @throws java.lang.IllegalArgumentException if the request is null
     */
    public SsdpServletRequestWrapper(SsdpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.request = request;
    }

    /**
     * Return the wrapped request object.
     *
     * @return SsdpServletRequest
     */
    public SsdpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Sets the request object being wrapped.
     *
     * @param request The request
     * @throws java.lang.IllegalArgumentException if the request is null.
     */
    public void setRequest(SsdpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        this.request = request;
    }

    /**
     * The default behavior of this method is to call getAttribute(String name)
     * on the wrapped request object.
     */
    @Override
    public <T> T getAttribute(String name) {
        return this.request.getAttribute(name);
    }


    /**
     * The default behavior of this method is to return getAttributeNames()
     * on the wrapped request object.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return this.request.getAttributeNames();
    }


    /**
     * The default behavior of this method is to return getCharacterEncoding()
     * on the wrapped request object.
     */
    @Override
    public String getCharacterEncoding() {
        return this.request.getCharacterEncoding();
    }

    /**
     * The default behavior of this method is to set the character encoding
     * on the wrapped request object.
     */
    @Override
    public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
        this.request.setCharacterEncoding(enc);
    }

    /**
     * The default behavior of this method is to return getInputStream()
     * on the wrapped request object.
     */
    @Override
    public SsdpServletInputStream getInputStream() throws IOException {
        return this.request.getInputStream();
    }


    /**
     * The default behavior of this method is to return getParameter(String name)
     * on the wrapped request object.
     */
    @Override
    public String getParameter(String name) {
        return this.request.getParameter(name);
    }

    /**
     * The default behavior of this method is to return getParameterMap()
     * on the wrapped request object.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return this.request.getParameterMap();
    }


    /**
     * The default behavior of this method is to return getParameterNames()
     * on the wrapped request object.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return this.request.getParameterNames();
    }


    /**
     * The default behavior of this method is to return getParameterValues(String name)
     * on the wrapped request object.
     */
    @Override
    public String[] getParameterValues(String name) {
        return this.request.getParameterValues(name);
    }


    /**
     * The default behavior of this method is to return getProtocol()
     * on the wrapped request object.
     */
    @Override
    public String getProtocol() {
        return this.request.getProtocol();
    }


    /**
     * The default behavior of this method is to return getScheme()
     * on the wrapped request object.
     */
    @Override
    public String getScheme() {
        return this.request.getScheme();
    }


    /**
     * The default behavior of this method is to return getServerName()
     * on the wrapped request object.
     */
    @Override
    public String getServerName() {
        return this.request.getServerName();
    }


    /**
     * The default behavior of this method is to return getServerPort()
     * on the wrapped request object.
     */
    @Override
    public int getServerPort() {
        return this.request.getServerPort();
    }


    /**
     * The default behavior of this method is to return getReader()
     * on the wrapped request object.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return this.request.getReader();
    }


    /**
     * The default behavior of this method is to return getRemoteAddr()
     * on the wrapped request object.
     */
    @Override
    public String getRemoteAddr() {
        return this.request.getRemoteAddr();
    }


    /**
     * The default behavior of this method is to return getRemoteHost()
     * on the wrapped request object.
     */
    @Override
    public String getRemoteHost() {
        return this.request.getRemoteHost();
    }


    /**
     * The default behavior of this method is to return setAttribute(String name, Object o)
     * on the wrapped request object.
     */
    @Override
    public void setAttribute(String name, Object o) {
        this.request.setAttribute(name, o);
    }


    /**
     * The default behavior of this method is to call removeAttribute(String name)
     * on the wrapped request object.
     */
    @Override
    public void removeAttribute(String name) {
        this.request.removeAttribute(name);
    }


    /**
     * The default behavior of this method is to return getLocale()
     * on the wrapped request object.
     */
    @Override
    public Locale getLocale() {
        return this.request.getLocale();
    }


    /**
     * The default behavior of this method is to return getLocales()
     * on the wrapped request object.
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return this.request.getLocales();
    }


    /**
     * The default behavior of this method is to return getRequestDispatcher(String path)
     * on the wrapped request object.
     */
    @Override
    public SsdpRequestDispatcher getRequestDispatcher(String path) {
        return this.request.getRequestDispatcher(path);
    }

    /**
     * The default behavior of this method is to return
     * getRemotePort() on the wrapped request object.
     */
    @Override
    public int getRemotePort() {
        return this.request.getRemotePort();
    }


    /**
     * The default behavior of this method is to return
     * getLocalName() on the wrapped request object.
     */
    @Override
    public String getLocalName() {
        return this.request.getLocalName();
    }

    /**
     * The default behavior of this method is to return
     * getLocalAddr() on the wrapped request object.
     */
    @Override
    public String getLocalAddr() {
        return this.request.getLocalAddr();
    }


    /**
     * The default behavior of this method is to return
     * getLocalPort() on the wrapped request object.
     */
    @Override
    public int getLocalPort() {
        return this.request.getLocalPort();
    }

}
