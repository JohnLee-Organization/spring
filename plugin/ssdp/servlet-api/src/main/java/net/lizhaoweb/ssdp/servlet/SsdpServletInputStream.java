/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 10:44
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides an input stream for reading binary data from a client
 * request, including an efficient <code>readLine</code> method
 * for reading data one line at a time. With some protocols, such
 * as HTTP POST and PUT, a <code>ServletInputStream</code>
 * object can be used to read data sent from the client.
 *
 * <p>A <code>ServletInputStream</code> object is normally retrieved via
 * the {@link SsdpServletRequest#getInputStream} method.
 *
 * <p>This is an abstract class that a servlet container implements.
 * Subclasses of this class
 * must implement the <code>java.io.InputStream.read()</code> method.
 * <p>
 * Created by jhon on 2024/6/4 10:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see SsdpServletRequest
 */
public abstract class SsdpServletInputStream extends InputStream {


    /**
     * Does nothing, because this is an abstract class.
     */
    protected SsdpServletInputStream() {
    }


    /**
     * Reads the input stream, one line at a time. Starting at an
     * offset, reads bytes into an array, until it reads a certain number
     * of bytes or reaches a newline character, which it reads into the
     * array as well.
     *
     * <p>This method returns -1 if it reaches the end of the input
     * stream before reading the maximum number of bytes.
     *
     * @param bytes an array of bytes into which data is read
     * @param off   an integer specifying the character at which
     *              this method begins reading
     * @param len   an integer specifying the maximum number of
     *              bytes to read
     * @return an integer specifying the actual number of bytes
     * read, or -1 if the end of the stream is reached
     * @throws IOException if an input or output exception has occurred
     */
    public int readLine(byte[] bytes, int off, int len) throws IOException {
        if (len <= 0) {
            return 0;
        }
        int count = 0, _char;
        while ((_char = read()) != -1) {
            bytes[off++] = (byte) _char;
            count++;
            if (_char == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

}
