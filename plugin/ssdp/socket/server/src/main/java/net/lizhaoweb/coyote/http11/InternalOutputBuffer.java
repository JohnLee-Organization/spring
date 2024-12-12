/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:59
 */
package net.lizhaoweb.coyote.http11;

import net.lizhaoweb.coyote.OutputBuffer;
import net.lizhaoweb.coyote.Response;
import net.lizhaoweb.mikefox.buf.ByteChunk;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Output buffer.
 * <p>
 * Created by jhon on 2024/6/7 16:59
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class InternalOutputBuffer extends AbstractOutputBuffer implements ByteChunk.ByteOutputChannel {

    // -------------------------------------------------------------- Constants


    // ----------------------------------------------------------- Constructors


    /**
     * Default constructor.
     */
    public InternalOutputBuffer(Response response) {
        this(response, Constants.DEFAULT_HTTP_HEADER_BUFFER_SIZE);
    }


    /**
     * Alternate constructor.
     */
    public InternalOutputBuffer(Response response, int headerBufferSize) {
        this.response = response;
        headers = response.getMimeHeaders();
        buf = new byte[headerBufferSize];
        outputStreamOutputBuffer = new OutputStreamOutputBuffer();
        filterLibrary = new OutputFilter[0];
        activeFilters = new OutputFilter[0];
        lastActiveFilter = -1;
        socketBuffer = new ByteChunk();
        socketBuffer.setByteOutputChannel(this);
        committed = false;
        finished = false;
    }

    /**
     * Underlying output stream.
     */
    protected OutputStream outputStream;


    /**
     * Socket buffer.
     */
    protected ByteChunk socketBuffer;


    /**
     * Socket buffer (extra buffering to reduce number of packets sent).
     */
    protected boolean useSocketBuffer = false;

    /**
     * Set the underlying socket output stream.
     */
    public void setOutputStream(OutputStream outputStream) {
        // FIXME: Check for null ?
        this.outputStream = outputStream;
    }


    /**
     * Get the underlying socket output stream.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }


    /**
     * Set the socket buffer size.
     */
    public void setSocketBuffer(int socketBufferSize) {
        if (socketBufferSize > 500) {
            useSocketBuffer = true;
            socketBuffer.allocate(socketBufferSize, socketBufferSize);
        } else {
            useSocketBuffer = false;
        }
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Flush the response.
     *
     * @throws IOException an underlying I/O error occurred
     */
    @Override
    public void flush() throws IOException {
        super.flush();
        // Flush the current buffer
        if (useSocketBuffer) {
            socketBuffer.flushBuffer();
        }
    }


    /**
     * Recycle the output buffer. This should be called when closing the
     * connection.
     */
    @Override
    public void recycle() {
        super.recycle();
        socketBuffer.recycle();
        outputStream = null;
    }


    /**
     * End processing of current HTTP request.
     * Note: All bytes of the current request should have been already
     * consumed. This method only resets all the pointers so that we are ready
     * to parse the next HTTP request.
     */
    @Override
    public void nextRequest() {
        super.nextRequest();
        socketBuffer.recycle();
    }


    /**
     * End request.
     *
     * @throws IOException an underlying I/O error occurred
     */
    @Override
    public void endRequest() throws IOException {
        super.endRequest();
        if (useSocketBuffer) {
            socketBuffer.flushBuffer();
        }
    }


    // ------------------------------------------------ HTTP/1.1 Output Methods


    /**
     * Send an acknowledgment.
     */
    @Override
    public void sendAck() throws IOException {
        if (!committed) outputStream.write(Constants.ACK_BYTES);
    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Commit the response.
     *
     * @throws IOException an underlying I/O error occurred
     */
    @Override
    protected void commit() throws IOException {
        // The response is now committed
        committed = true;
        response.setCommitted(true);
        if (pos > 0) {
            // Sending the response header buffer
            if (useSocketBuffer) {
                socketBuffer.append(buf, 0, pos);
            } else {
                outputStream.write(buf, 0, pos);
            }
        }
    }


    /**
     * Callback to write data from the buffer.
     */
    public void realWriteBytes(byte[] cbuf, int off, int len) throws IOException {
        if (len > 0) {
            outputStream.write(cbuf, off, len);
        }
    }


    // ----------------------------------- OutputStreamOutputBuffer Inner Class


    /**
     * This class is an output buffer which will write data to an output
     * stream.
     */
    protected class OutputStreamOutputBuffer implements OutputBuffer {


        /**
         * Write chunk.
         */
        public int doWrite(ByteChunk chunk, Response res) throws IOException {
            int length = chunk.getLength();
            if (useSocketBuffer) {
                socketBuffer.append(chunk.getBuffer(), chunk.getStart(), length);
            } else {
                outputStream.write(chunk.getBuffer(), chunk.getStart(), length);
            }
            return length;
        }


    }

}
