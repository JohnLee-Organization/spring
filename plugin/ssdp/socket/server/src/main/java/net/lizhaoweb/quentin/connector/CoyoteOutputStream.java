/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin.connector
 * @date : 2024-06-11
 * @time : 09:45
 */
package net.lizhaoweb.quentin.connector;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * Coyote implementation of the servlet output stream.
 * <p>
 * Created by jhon on 2024/6/11 9:45
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class CoyoteOutputStream extends ServletOutputStream {


    // ----------------------------------------------------- Instance Variables


    protected OutputBuffer ob;


    // ----------------------------------------------------------- Constructors


    protected CoyoteOutputStream(OutputBuffer ob) {
        this.ob = ob;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Prevent cloning the facade.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    // -------------------------------------------------------- Package Methods


    /**
     * Clear facade.
     */
    void clear() {
        ob = null;
    }


    // --------------------------------------------------- OutputStream Methods


    @Override
    public void write(int i) throws IOException {
        ob.writeByte(i);
    }


    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }


    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        ob.write(b, off, len);
    }


    /**
     * Will send the buffer to the client.
     */
    @Override
    public void flush() throws IOException {
        ob.flush();
    }


    @Override
    public void close() throws IOException {
        ob.close();
    }

}
