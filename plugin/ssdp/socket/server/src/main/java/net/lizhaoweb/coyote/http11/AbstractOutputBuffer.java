/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:41
 */
package net.lizhaoweb.coyote.http11;

import net.lizhaoweb.coyote.ActionCode;
import net.lizhaoweb.coyote.OutputBuffer;
import net.lizhaoweb.coyote.Response;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.buf.CharChunk;
import net.lizhaoweb.mikefox.buf.MessageBytes;
import net.lizhaoweb.mikefox.http.HttpMessages;
import net.lizhaoweb.mikefox.http.MimeHeaders;
import net.lizhaoweb.mikefox.res.StringManager;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by jhon on 2024/6/7 16:41
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public abstract class AbstractOutputBuffer implements OutputBuffer {

    // ----------------------------------------------------- Instance Variables


    /**
     * Associated Coyote response.
     */
    protected Response response;


    /**
     * Headers of the associated request.
     */
    protected MimeHeaders headers;


    /**
     * Committed flag.
     */
    protected boolean committed;


    /**
     * Finished flag.
     */
    protected boolean finished;


    /**
     * The buffer used for header composition.
     */
    protected byte[] buf;


    /**
     * Position in the buffer.
     */
    protected int pos;


    /**
     * Filter library.
     * Note: Filter[0] is always the "chunked" filter.
     */
    protected OutputFilter[] filterLibrary;


    /**
     * Active filter (which is actually the top of the pipeline).
     */
    protected OutputFilter[] activeFilters;


    /**
     * Index of the last active filter.
     */
    protected int lastActiveFilter;

    /**
     * Underlying output buffer.
     */
    protected OutputBuffer outputStreamOutputBuffer;

    // -------------------------------------------------------------- Variables


    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);

    // ------------------------------------------------------------- Properties


    /**
     * Add an output filter to the filter library.
     */
    public void addFilter(OutputFilter filter) {
        OutputFilter[] newFilterLibrary = new OutputFilter[filterLibrary.length + 1];
        System.arraycopy(filterLibrary, 0, newFilterLibrary, 0, filterLibrary.length);
        newFilterLibrary[filterLibrary.length] = filter;
        filterLibrary = newFilterLibrary;
        activeFilters = new OutputFilter[filterLibrary.length];
    }


    /**
     * Get filters.
     */
    public OutputFilter[] getFilters() {
        return filterLibrary;
    }


    /**
     * Clear filters.
     */
    public void clearFilters() {
        filterLibrary = new OutputFilter[0];
        lastActiveFilter = -1;
    }


    /**
     * Add an output filter to the filter library.
     */
    public void addActiveFilter(OutputFilter filter) {
        if (lastActiveFilter == -1) {
            filter.setBuffer(outputStreamOutputBuffer);
        } else {
            for (int i = 0; i <= lastActiveFilter; i++) {
                if (activeFilters[i] == filter) return;
            }
            filter.setBuffer(activeFilters[lastActiveFilter]);
        }
        activeFilters[++lastActiveFilter] = filter;
        filter.setResponse(response);
    }


    // --------------------------------------------------- OutputBuffer Methods


    /**
     * Write the contents of a byte chunk.
     *
     * @param chunk byte chunk
     * @return number of bytes written
     * @throws IOException an underlying I/O error occurred
     */
    public int doWrite(ByteChunk chunk, Response res) throws IOException {
        if (!committed) {
            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeaders) and
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);
        }
        if (lastActiveFilter == -1) return outputStreamOutputBuffer.doWrite(chunk, res);
        else return activeFilters[lastActiveFilter].doWrite(chunk, res);

    }

    // --------------------------------------------------------- Public Methods


    /**
     * Flush the response.
     *
     * @throws IOException an underlying I/O error occurred
     */
    public void flush() throws IOException {
        if (!committed) {
            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeader) and
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);
        }
    }

    /**
     * Reset current response.
     *
     * @throws IllegalStateException if the response has already been committed
     */
    public void reset() {
        if (committed) throw new IllegalStateException(/*FIXME:Put an error message*/);
        // Recycle Request object
        response.recycle();
    }

    /**
     * Recycle the output buffer. This should be called when closing the
     * connection.
     */
    public void recycle() {
        // Recycle filters
        for (int i = 0; i <= lastActiveFilter; i++) {
            activeFilters[i].recycle();
        }
        // Recycle Request object
        response.recycle();
        pos = 0;
        lastActiveFilter = -1;
        committed = false;
        finished = false;
    }

    /**
     * End processing of current HTTP request.
     * Note: All bytes of the current request should have been already
     * consumed. This method only resets all the pointers so that we are ready
     * to parse the next HTTP request.
     */
    public void nextRequest() {

        // Recycle Request object
        response.recycle();
        // Recycle filters
        for (int i = 0; i <= lastActiveFilter; i++) {
            activeFilters[i].recycle();
        }

        // Reset pointers
        pos = 0;
        lastActiveFilter = -1;
        committed = false;
        finished = false;
    }

    /**
     * End request.
     *
     * @throws IOException an underlying I/O error occurred
     */
    public void endRequest() throws IOException {
        if (!committed) {
            // Send the connector a request for commit. The connector should
            // then validate the headers, send them (using sendHeader) and
            // set the filters accordingly.
            response.action(ActionCode.ACTION_COMMIT, null);

        }
        if (finished) return;
        if (lastActiveFilter != -1) activeFilters[lastActiveFilter].end();
        finished = true;
    }

    public abstract void sendAck() throws IOException;

    protected abstract void commit() throws IOException;


    /**
     * Send the response status line.
     */
    public void sendStatus() {

        // Write protocol name
        write(Constants.HTTP_11_BYTES);
        buf[pos++] = Constants.SP;

        // Write status code
        int status = response.getStatus();
        switch (status) {
            case 200:
                write(Constants._200_BYTES);
                break;
            case 400:
                write(Constants._400_BYTES);
                break;
            case 404:
                write(Constants._404_BYTES);
                break;
            default:
                write(status);
        }
        buf[pos++] = Constants.SP;

        // Write message
        String message = null;
        if (net.lizhaoweb.coyote.Constants.USE_CUSTOM_STATUS_MSG_IN_HEADER && HttpMessages.isSafeInHttpHeader(response.getMessage())) {
            message = response.getMessage();
        }
        if (message == null) {
            write(HttpMessages.getMessage(status));
        } else {
            write(message);
        }

        // End the response status line
        if (net.lizhaoweb.coyote.Constants.IS_SECURITY_ENABLED) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    buf[pos++] = Constants.CR;
                    buf[pos++] = Constants.LF;
                    return null;
                }
            });
        } else {
            buf[pos++] = Constants.CR;
            buf[pos++] = Constants.LF;
        }
    }


    /**
     * Send a header.
     *
     * @param name  Header name
     * @param value Header value
     */
    public void sendHeader(MessageBytes name, MessageBytes value) {
        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;
    }


    /**
     * Send a header.
     *
     * @param name  Header name
     * @param value Header value
     */
    public void sendHeader(ByteChunk name, ByteChunk value) {
        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;
    }


    /**
     * Send a header.
     *
     * @param name  Header name
     * @param value Header value
     */
    public void sendHeader(String name, String value) {
        write(name);
        buf[pos++] = Constants.COLON;
        buf[pos++] = Constants.SP;
        write(value);
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;
    }


    /**
     * End the header block.
     */
    public void endHeaders() {
        buf[pos++] = Constants.CR;
        buf[pos++] = Constants.LF;
    }


    /**
     * This method will write the contents of the specified message bytes
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     *
     * @param mb data to be written
     */
    protected void write(MessageBytes mb) {
        if (mb.getType() == MessageBytes.T_BYTES) {
            ByteChunk bc = mb.getByteChunk();
            write(bc);
        } else if (mb.getType() == MessageBytes.T_CHARS) {
            CharChunk cc = mb.getCharChunk();
            write(cc);
        } else {
            write(mb.toString());
        }
    }


    /**
     * This method will write the contents of the specified message bytes
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     *
     * @param bc data to be written
     */
    protected void write(ByteChunk bc) {
        // Writing the byte chunk to the output buffer
        int length = bc.getLength();
        System.arraycopy(bc.getBytes(), bc.getStart(), buf, pos, length);
        pos = pos + length;
    }


    /**
     * This method will write the contents of the specified char
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     *
     * @param cc data to be written
     */
    protected void write(CharChunk cc) {
        int start = cc.getStart();
        int end = cc.getEnd();
        char[] cbuf = cc.getBuffer();
        for (int i = start; i < end; i++) {
            char c = cbuf[i];
            // Note:  This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework.  It must suffice until servlet output
            // streams properly encode their output.
            if ((c <= 31) && (c != 9)) {
                c = ' ';
            } else if (c == 127) {
                c = ' ';
            }
            buf[pos++] = (byte) c;
        }
    }


    /**
     * This method will write the contents of the specified byte
     * buffer to the output stream, without filtering. This method is meant to
     * be used to write the response header.
     *
     * @param b data to be written
     */
    public void write(byte[] b) {
        // Writing the byte chunk to the output buffer
        System.arraycopy(b, 0, buf, pos, b.length);
        pos = pos + b.length;
    }


    /**
     * This method will write the contents of the specified String to the
     * output stream, without filtering. This method is meant to be used to
     * write the response header.
     *
     * @param s data to be written
     */
    protected void write(String s) {
        if (s == null) return;
        // From the Tomcat 3.3 HTTP/1.0 connector
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            // Note:  This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework.  It must suffice until servlet output
            // streams properly encode their output.
            if ((c <= 31) && (c != 9)) {
                c = ' ';
            } else if (c == 127) {
                c = ' ';
            }
            buf[pos++] = (byte) c;
        }
    }


    /**
     * This method will print the specified integer to the output stream,
     * without filtering. This method is meant to be used to write the
     * response header.
     *
     * @param i data to be written
     */
    protected void write(int i) {
        write(String.valueOf(i));
    }

}
