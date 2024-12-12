/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:31
 */
package net.lizhaoweb.coyote.http11;

import net.lizhaoweb.coyote.InputBuffer;
import net.lizhaoweb.coyote.Request;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.buf.MessageBytes;

import java.io.EOFException;
import java.io.IOException;

/**
 * Implementation of InputBuffer which provides HTTP request header parsing as
 * well as transfer decoding.
 * <p>
 * Created by jhon on 2024/6/7 16:31
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class InternalInputBuffer extends AbstractInputBuffer {

    /**
     * Default constructor.
     */
    public InternalInputBuffer(Request request) {
        this(request, Constants.DEFAULT_HTTP_HEADER_BUFFER_SIZE);
    }


    /**
     * Alternate constructor.
     */
    public InternalInputBuffer(Request request, int headerBufferSize) {
        this.request = request;
        headers = request.getMimeHeaders();
        buf = new byte[headerBufferSize];
        inputStreamInputBuffer = new InputStreamInputBuffer();
        filterLibrary = new InputFilter[0];
        activeFilters = new InputFilter[0];
        lastActiveFilter = -1;
        parsingHeader = true;
        swallowInput = true;
    }

    /**
     * Read the request line. This function is meant to be used during the
     * HTTP request header parsing. Do NOT attempt to read the request body
     * using it.
     *
     * @throws IOException If an exception occurs during the underlying socket
     *                     read operations, or if the given buffer is not big enough to accommodate
     *                     the whole line.
     */
    @Override
    public boolean parseRequestLine(boolean useAvailableDataOnly) throws IOException {
        int start = 0;
        //
        // Skipping blank lines
        //
        byte chr = 0;
        do {
            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }
            chr = buf[pos++];
        } while ((chr == Constants.CR) || (chr == Constants.LF));
        pos--;

        // Mark the current buffer position
        start = pos;

        //
        // Reading the method name
        // Method name is always US-ASCII
        //
        boolean space = false;

        while (!space) {

            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            // Spec says no CR or LF in method name
            if (buf[pos] == Constants.CR || buf[pos] == Constants.LF) {
                throw new IllegalArgumentException(sm.getString("iib.invalidmethod"));
            }
            // Spec says single SP but it also says be tolerant of HT
            if (buf[pos] == Constants.SP || buf[pos] == Constants.HT) {
                space = true;
                request.method().setBytes(buf, start, pos - start);
            }
            pos++;
        }


        // Spec says single SP but also says be tolerant of multiple and/or HT
        while (space) {
            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }
            if (buf[pos] == Constants.SP || buf[pos] == Constants.HT) {
                pos++;
            } else {
                space = false;
            }
        }

        // Mark the current buffer position
        start = pos;
        int end = 0;
        int questionPos = -1;

        //
        // Reading the URI
        //

        boolean eol = false;

        while (!space) {

            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            // Spec says single SP but it also says be tolerant of HT
            if (buf[pos] == Constants.SP || buf[pos] == Constants.HT) {
                space = true;
                end = pos;
            } else if ((buf[pos] == Constants.CR) || (buf[pos] == Constants.LF)) {
                // HTTP/0.9 style request
                eol = true;
                space = true;
                end = pos;
            } else if ((buf[pos] == Constants.QUESTION) && (questionPos == -1)) {
                questionPos = pos;
            }

            pos++;

        }

        request.unparsedURI().setBytes(buf, start, end - start);
        if (questionPos >= 0) {
            request.queryString().setBytes(buf, questionPos + 1, end - questionPos - 1);
            request.requestURI().setBytes(buf, start, questionPos - start);
        } else {
            request.requestURI().setBytes(buf, start, end - start);
        }

        // Spec says single SP but also says be tolerant of multiple and/or HT
        while (space) {
            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }
            if (buf[pos] == Constants.SP || buf[pos] == Constants.HT) {
                pos++;
            } else {
                space = false;
            }
        }

        // Mark the current buffer position
        start = pos;
        end = 0;

        //
        // Reading the protocol
        // Protocol is always US-ASCII
        //

        while (!eol) {

            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            if (buf[pos] == Constants.CR) {
                end = pos;
            } else if (buf[pos] == Constants.LF) {
                if (end == 0) end = pos;
                eol = true;
            }

            pos++;

        }

        if ((end - start) > 0) {
            request.protocol().setBytes(buf, start, end - start);
        } else {
            request.protocol().setString("");
        }

        return true;

    }


    /**
     * Parse the HTTP headers.
     */
    @Override
    public boolean parseHeaders() throws IOException {
        while (parseHeader()) ;
        parsingHeader = false;
        end = pos;
        return true;
    }


    /**
     * Parse an HTTP header.
     *
     * @return false after reading a blank line (which indicates that the
     * HTTP header parsing is done
     */
    public boolean parseHeader() throws IOException {
        //
        // Check for blank line
        //
        byte chr = 0;
        while (true) {
            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            chr = buf[pos];
            if ((chr == Constants.CR) || (chr == Constants.LF)) {
                if (chr == Constants.LF) {
                    pos++;
                    return false;
                }
            } else {
                break;
            }
            pos++;
        }

        // Mark the current buffer position
        int start = pos;

        //
        // Reading the header name
        // Header name is always US-ASCII
        //
        boolean colon = false;
        MessageBytes headerValue = null;

        while (!colon) {

            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            if (buf[pos] == Constants.COLON) {
                colon = true;
                headerValue = headers.addValue(buf, start, pos - start);
            }
            chr = buf[pos];
            if ((chr >= Constants.A) && (chr <= Constants.Z)) {
                buf[pos] = (byte) (chr - Constants.LC_OFFSET);
            }
            pos++;
        }

        // Mark the current buffer position
        start = pos;
        int realPos = pos;

        //
        // Reading the header value (which can be spanned over multiple lines)
        //
        boolean eol = false;
        boolean validLine = true;

        while (validLine) {
            boolean space = true;

            // Skipping spaces
            while (space) {

                // Read new bytes if needed
                if (pos >= lastValid) {
                    if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
                }

                if ((buf[pos] == Constants.SP) || (buf[pos] == Constants.HT)) {
                    pos++;
                } else {
                    space = false;
                }
            }
            int lastSignificantChar = realPos;

            // Reading bytes until the end of the line
            while (!eol) {

                // Read new bytes if needed
                if (pos >= lastValid) {
                    if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
                }

//                if (buf[pos] == Constants.CR) {
//                } else
                if (buf[pos] == Constants.LF) {
                    eol = true;
                } else if (buf[pos] == Constants.SP) {
                    buf[realPos] = buf[pos];
                    realPos++;
                } else {
                    buf[realPos] = buf[pos];
                    realPos++;
                    lastSignificantChar = realPos;
                }
                pos++;
            }
            realPos = lastSignificantChar;

            // Checking the first character of the new line. If the character
            // is a LWS, then it's a multiline header

            // Read new bytes if needed
            if (pos >= lastValid) {
                if (!fill()) throw new EOFException(sm.getString("iib.eof.error"));
            }

            chr = buf[pos];
            if ((chr != Constants.SP) && (chr != Constants.HT)) {
                validLine = false;
            } else {
                eol = false;
                // Copying one extra space in the buffer (since there must
                // be at least one space inserted between the lines)
                buf[realPos] = chr;
                realPos++;
            }
        }
        // Set the header value
        headerValue.setBytes(buf, start, realPos - start);
        return true;
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Fill the internal buffer using data from the underlying input stream.
     *
     * @return false if at end of stream
     */
    protected boolean fill() throws IOException {
        return fill(true);
    }

    @Override
    protected boolean fill(boolean block) throws IOException {
        int nRead = 0;
        if (parsingHeader) {
            if (lastValid == buf.length) {
                throw new IllegalArgumentException(sm.getString("iib.requestheadertoolarge.error"));
            }
            nRead = inputStream.read(buf, pos, buf.length - lastValid);
            if (nRead > 0) {
                lastValid = pos + nRead;
            }
        } else {
            if (buf.length - end < 4500) {
                // In this case, the request header was really large, so we allocate a
                // brand new one; the old one will get GCed when subsequent requests
                // clear all references
                buf = new byte[buf.length];
                end = 0;
            }
            pos = end;
            lastValid = pos;
            nRead = inputStream.read(buf, pos, buf.length - lastValid);
            if (nRead > 0) {
                lastValid = pos + nRead;
            }
        }
        return (nRead > 0);
    }


    // ------------------------------------- InputStreamInputBuffer Inner Class


    /**
     * This class is an input buffer which will read its data from an input
     * stream.
     */
    protected class InputStreamInputBuffer implements InputBuffer {


        /**
         * Read bytes into the specified chunk.
         */
        public int doRead(ByteChunk chunk, Request req) throws IOException {
            if (pos >= lastValid) {
                if (!fill()) return -1;
            }
            int length = lastValid - pos;
            chunk.setBytes(buf, pos, length);
            pos = lastValid;
            return (length);
        }


    }

}
