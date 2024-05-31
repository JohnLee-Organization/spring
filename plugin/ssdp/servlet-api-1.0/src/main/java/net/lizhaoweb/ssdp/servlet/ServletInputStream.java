/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 14:50
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
 * the {@link ServletRequest#getInputStream} method.
 *
 * <p>This is an abstract class that a servlet container implements.
 * Subclasses of this class
 * must implement the <code>java.io.InputStream.read()</code> method.
 * <p>
 * Created by jhon on 2024/5/30 14:50
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see ServletRequest
 */
@SuppressWarnings("unused")
public abstract class ServletInputStream extends InputStream {


    /**
     * Does nothing, because this is an abstract class.
     */
    protected ServletInputStream() {
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
     * @param b   an array of bytes into which data is read
     * @param off an integer specifying the character at which
     *            this method begins reading
     * @param len an integer specifying the maximum number of
     *            bytes to read
     * @return an integer specifying the actual number of bytes
     * read, or -1 if the end of the stream is reached
     * @throws IOException if an input or output exception has occurred
     */
    public int readLine(byte[] b, int off, int len) throws IOException {

        if (len <= 0) {
            return 0;
        }
        int count = 0, c;

        while ((c = read()) != -1) {
            b[off++] = (byte) c;
            count++;
            if (c == '\n' || count == len) {
                break;
            }
        }
        return count > 0 ? count : -1;
    }

}
