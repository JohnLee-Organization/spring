/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 11:43
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Provides a convenient implementation of the ServletResponse interface that
 * can be subclassed by developers wishing to adapt the response from a Servlet.
 * This class implements the Wrapper or Decorator pattern. Methods default to
 * calling through to the wrapped response object.
 * <p>
 * Created by jhon on 2024/6/4 11:43
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see SsdpServletResponse
 */
public class SsdpServletResponseWrapper implements SsdpServletResponse {

    private SsdpServletResponse response;

    /**
     * Creates a ServletResponse adaptor wrapping the given response object.
     *
     * @throws java.lang.IllegalArgumentException if the response is null.
     */
    public SsdpServletResponseWrapper(SsdpServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }
        this.response = response;
    }

    /**
     * Return the wrapped ServletResponse object.
     */
    public SsdpServletResponse getResponse() {
        return this.response;
    }


    /**
     * Sets the response being wrapped.
     *
     * @throws java.lang.IllegalArgumentException if the response is null.
     */
    public void setResponse(SsdpServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }
        this.response = response;
    }

    /**
     * The default behavior of this method is to call setCharacterEncoding(String charset)
     * on the wrapped response object.
     */
    @Override
    public void setCharacterEncoding(String charset) {
        this.response.setCharacterEncoding(charset);
    }

    /**
     * The default behavior of this method is to return getCharacterEncoding()
     * on the wrapped response object.
     */
    @Override
    public String getCharacterEncoding() {
        return this.response.getCharacterEncoding();
    }


    /**
     * The default behavior of this method is to return getOutputStream()
     * on the wrapped response object.
     */
    @Override
    public SsdpServletOutputStream getOutputStream() throws IOException {
        return this.response.getOutputStream();
    }

    /**
     * The default behavior of this method is to return getWriter()
     * on the wrapped response object.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return this.response.getWriter();
    }

    /**
     * The default behavior of this method is to call setContentLength(int len)
     * on the wrapped response object.
     */
    @Override
    public void setContentLength(int len) {
        this.response.setContentLength(len);
    }

    /**
     * The default behavior of this method is to call setBufferSize(int size)
     * on the wrapped response object.
     */
    @Override
    public void setBufferSize(int size) {
        this.response.setBufferSize(size);
    }

    /**
     * The default behavior of this method is to return getBufferSize()
     * on the wrapped response object.
     */
    @Override
    public int getBufferSize() {
        return this.response.getBufferSize();
    }

    /**
     * The default behavior of this method is to call flushBuffer()
     * on the wrapped response object.
     */
    @Override
    public void flushBuffer() throws IOException {
        this.response.flushBuffer();
    }

    /**
     * The default behavior of this method is to return isCommitted()
     * on the wrapped response object.
     */
    @Override
    public boolean isCommitted() {
        return this.response.isCommitted();
    }

    /**
     * The default behavior of this method is to call reset()
     * on the wrapped response object.
     */
    @Override
    public void reset() {
        this.response.reset();
    }

    /**
     * The default behavior of this method is to call resetBuffer()
     * on the wrapped response object.
     */
    @Override
    public void resetBuffer() {
        this.response.resetBuffer();
    }

    /**
     * The default behavior of this method is to call setLocale(Locale loc)
     * on the wrapped response object.
     */
    @Override
    public void setLocale(Locale loc) {
        this.response.setLocale(loc);
    }

    /**
     * The default behavior of this method is to return getLocale()
     * on the wrapped response object.
     */
    @Override
    public Locale getLocale() {
        return this.response.getLocale();
    }

    @Override
    public void setContentType(String contentType) {
        this.response.setContentType(contentType);
    }

}
