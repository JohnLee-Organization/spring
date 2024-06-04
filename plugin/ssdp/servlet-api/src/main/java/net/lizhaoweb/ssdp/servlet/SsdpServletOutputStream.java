/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 10:56
 */
package net.lizhaoweb.ssdp.servlet;

import net.lizhaoweb.ssdp.servlet.utils.Constants;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Provides an output stream for sending binary data to the
 * client. A <code>ServletOutputStream</code> object is normally retrieved
 * via the {@link SsdpServletResponse#getOutputStream} method.
 *
 * <p>This is an abstract class that the servlet container implements.
 * Subclasses of this class
 * must implement the <code>java.io.OutputStream.write(int)</code>
 * method.
 * <p>
 * Created by jhon on 2024/6/4 10:56
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see SsdpServletResponse
 */
public abstract class SsdpServletOutputStream extends OutputStream {

    private static final String LSTRING_FILE = "net.lizhaoweb.ssdp.servlet.LocalStrings";
    private static ResourceBundle lStrings = ResourceBundle.getBundle(LSTRING_FILE);


    /**
     * Does nothing, because this is an abstract class.
     */

    protected SsdpServletOutputStream() {
    }


    /**
     * Writes a <code>String</code> to the client,
     * without a carriage return-line feed (CRLF)
     * character at the end.
     *
     * @param string the <code>String</code> to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(String string) throws IOException {
        if (string == null) string = "null";
        int len = string.length();
        for (int index = 0; index < len; index++) {
            char _char = string.charAt(index);

            //
            // XXX NOTE:  This is clearly incorrect for many strings,
            // but is the only consistent approach within the current
            // servlet framework.  It must suffice until servlet output
            // streams properly encode their output.
            //
            if ((_char & 0xff00) != 0) {    // high order byte must be zero
                String errMsg = lStrings.getString("err.not_iso8859_1");
                Object[] errArgs = new Object[1];
                errArgs[0] = _char;
                errMsg = MessageFormat.format(errMsg, errArgs);
                throw new CharConversionException(errMsg);
            }
            write(_char);
        }
    }


    /**
     * Writes a <code>boolean</code> value to the client,
     * with no carriage return-line feed (CRLF)
     * character at the end.
     *
     * @param bool the <code>boolean</code> value
     *             to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(boolean bool) throws IOException {
        String msg;
        if (bool) {
            msg = lStrings.getString("value.true");
        } else {
            msg = lStrings.getString("value.false");
        }
        print(msg);
    }


    /**
     * Writes a character to the client,
     * with no carriage return-line feed (CRLF)
     * at the end.
     *
     * @param cha the character to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(char cha) throws IOException {
        print(String.valueOf(cha));
    }


    /**
     * Writes an int to the client,
     * with no carriage return-line feed (CRLF)
     * at the end.
     *
     * @param i the int to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(int i) throws IOException {
        print(String.valueOf(i));
    }


    /**
     * Writes a <code>long</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param l the <code>long</code> value
     *          to send to the client
     * @throws IOException if an input or output exception
     *                     occurred
     */
    public void print(long l) throws IOException {
        print(String.valueOf(l));
    }


    /**
     * Writes a <code>float</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param f the <code>float</code> value
     *          to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(float f) throws IOException {
        print(String.valueOf(f));
    }


    /**
     * Writes a <code>double</code> value to the client,
     * with no carriage return-line feed (CRLF) at the end.
     *
     * @param d the <code>double</code> value
     *          to send to the client
     * @throws IOException if an input or output exception occurred
     */
    public void print(double d) throws IOException {
        print(String.valueOf(d));
    }


    /**
     * Writes a carriage return-line feed (CRLF)
     * to the client.
     *
     * @throws IOException if an input or output exception occurred
     */
    public void println() throws IOException {
        print(Constants.CRLF);
    }


    /**
     * Writes a <code>String</code> to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param s the <code>String</code> to write to the client
     * @throws IOException if an input or output exception occurred
     */
    public void println(String s) throws IOException {
        print(s);
        println();
    }


    /**
     * Writes a <code>boolean</code> value to the client,
     * followed by a
     * carriage return-line feed (CRLF).
     *
     * @param b the <code>boolean</code> value
     *          to write to the client
     * @throws IOException if an input or output exception occurred
     */
    public void println(boolean b) throws IOException {
        print(b);
        println();
    }


    /**
     * Writes a character to the client, followed by a carriage
     * return-line feed (CRLF).
     *
     * @param c the character to write to the client
     * @throws IOException if an input or output exception occurred
     */

    public void println(char c) throws IOException {
        print(c);
        println();
    }


    /**
     * Writes an int to the client, followed by a
     * carriage return-line feed (CRLF) character.
     *
     * @param i the int to write to the client
     * @throws IOException if an input or output exception occurred
     */
    public void println(int i) throws IOException {
        print(i);
        println();
    }


    /**
     * Writes a <code>long</code> value to the client, followed by a
     * carriage return-line feed (CRLF).
     *
     * @param l the <code>long</code> value to write to the client
     * @throws IOException if an input or output exception occurred
     */
    public void println(long l) throws IOException {
        print(l);
        println();
    }


    /**
     * Writes a <code>float</code> value to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param f the <code>float</code> value
     *          to write to the client
     * @throws IOException if an input or output exception
     *                     occurred
     */
    public void println(float f) throws IOException {
        print(f);
        println();
    }


    /**
     * Writes a <code>double</code> value to the client,
     * followed by a carriage return-line feed (CRLF).
     *
     * @param d the <code>double</code> value
     *          to write to the client
     * @throws IOException if an input or output exception occurred
     */
    public void println(double d) throws IOException {
        print(d);
        println();
    }

}
