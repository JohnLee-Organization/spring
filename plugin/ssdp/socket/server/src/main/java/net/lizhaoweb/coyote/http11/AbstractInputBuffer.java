/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:22
 */
package net.lizhaoweb.coyote.http11;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.coyote.InputBuffer;
import net.lizhaoweb.coyote.Request;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.http.MimeHeaders;
import net.lizhaoweb.mikefox.res.StringManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jhon on 2024/6/7 16:22
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public abstract class AbstractInputBuffer implements InputBuffer {

    public abstract boolean parseRequestLine(boolean useAvailableDataOnly) throws IOException;

    public abstract boolean parseHeaders() throws IOException;

    protected abstract boolean fill(boolean block) throws IOException;

    // -------------------------------------------------------------- Constants


    // ----------------------------------------------------------- Constructors


    // -------------------------------------------------------------- Variables


    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);


    // ----------------------------------------------------- Instance Variables


    /**
     * Associated Coyote request.
     */
    protected Request request;


    /**
     * Headers of the associated request.
     */
    protected MimeHeaders headers;


    /**
     * State.
     */
    protected boolean parsingHeader;


    /**
     * Swallow input ? (in the case of an expectation)
     */
    @Setter
    protected boolean swallowInput;


    /**
     * Pointer to the current read buffer.
     */
    protected byte[] buf;


    /**
     * Last valid byte.
     */
    protected int lastValid;


    /**
     * Position in the buffer.
     */
    protected int pos;


    /**
     * Pos of the end of the header in the buffer, which is also the
     * start of the body.
     */
    protected int end;


    /**
     * Underlying input stream.
     */
    @Setter
    @Getter
    protected InputStream inputStream;


    /**
     * Underlying input buffer.
     */
    protected InputBuffer inputStreamInputBuffer;


    /**
     * Filter library.
     * Note: Filter[0] is always the "chunked" filter.
     */
    protected InputFilter[] filterLibrary;


    /**
     * Active filters (in order).
     */
    protected InputFilter[] activeFilters;


    /**
     * Index of the last active filter.
     */
    protected int lastActiveFilter;


    // ------------------------------------------------------------- Properties


    /**
     * Add an input filter to the filter library.
     */
    public void addFilter(InputFilter filter) {
        // FIXME: Check for null ?
        InputFilter[] newFilterLibrary = new InputFilter[filterLibrary.length + 1];
        System.arraycopy(filterLibrary, 0, newFilterLibrary, 0, filterLibrary.length);
        newFilterLibrary[filterLibrary.length] = filter;
        filterLibrary = newFilterLibrary;
        activeFilters = new InputFilter[filterLibrary.length];
    }


    /**
     * Get filters.
     */
    public InputFilter[] getFilters() {
        return filterLibrary;
    }


    /**
     * Clear filters.
     */
    public void clearFilters() {
        filterLibrary = new InputFilter[0];
        lastActiveFilter = -1;
    }


    /**
     * Add an input filter to the filter library.
     */
    public void addActiveFilter(InputFilter filter) {
        if (lastActiveFilter == -1) {
            filter.setBuffer(inputStreamInputBuffer);
        } else {
            for (int i = 0; i <= lastActiveFilter; i++) {
                if (activeFilters[i] == filter) return;
            }
            filter.setBuffer(activeFilters[lastActiveFilter]);
        }
        activeFilters[++lastActiveFilter] = filter;
        filter.setRequest(request);
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Recycle the input buffer. This should be called when closing the
     * connection.
     */
    public void recycle() {
        // Recycle Request object
        request.recycle();
        inputStream = null;
        lastValid = 0;
        pos = 0;
        lastActiveFilter = -1;
        parsingHeader = true;
        swallowInput = true;
    }


    /**
     * End processing of current HTTP request.
     * Note: All bytes of the current request should have been already
     * consumed. This method only resets all the pointers so that we are ready
     * to parse the next HTTP request.
     */
    public void nextRequest() {
        // Recycle Request object
        request.recycle();

        // Copy leftover bytes to the beginning of the buffer
        if (lastValid - pos > 0) {
            int npos = 0;
            int opos = pos;
            while (lastValid - opos > opos - npos) {
                System.arraycopy(buf, opos, buf, npos, opos - npos);
                npos += pos;
                opos += pos;
            }
            System.arraycopy(buf, opos, buf, npos, lastValid - opos);
        }

        // Recycle filters
        for (int i = 0; i <= lastActiveFilter; i++) {
            activeFilters[i].recycle();
        }

        // Reset pointers
        lastValid = lastValid - pos;
        pos = 0;
        lastActiveFilter = -1;
        parsingHeader = true;
        swallowInput = true;
    }


    /**
     * End request (consumes leftover bytes).
     *
     * @throws IOException an underlying I/O error occurred
     */
    public void endRequest() throws IOException {
        if (swallowInput && (lastActiveFilter != -1)) {
            int extraBytes = (int) activeFilters[lastActiveFilter].end();
            pos = pos - extraBytes;
        }
    }

    // ---------------------------------------------------- InputBuffer Methods


    /**
     * Read some bytes.
     */
    public int doRead(ByteChunk chunk, Request req) throws IOException {
        if (lastActiveFilter == -1) return inputStreamInputBuffer.doRead(chunk, req);
        else return activeFilters[lastActiveFilter].doRead(chunk, req);
    }

}
