/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:26
 */
package net.lizhaoweb.coyote.http11;

import net.lizhaoweb.coyote.InputBuffer;
import net.lizhaoweb.coyote.Request;
import net.lizhaoweb.mikefox.buf.ByteChunk;

import java.io.IOException;

/**
 * Input filter interface.
 * <p>
 * Created by jhon on 2024/6/7 16:26
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface InputFilter extends InputBuffer {


    /**
     * Read bytes.
     *
     * @return Number of bytes read.
     */
    int doRead(ByteChunk chunk, Request unused) throws IOException;


    /**
     * Some filters need additional parameters from the request. All the
     * necessary reading can occur in that method, as this method is called
     * after the request header processing is complete.
     */
    void setRequest(Request request);


    /**
     * Make the filter ready to process the next request.
     */
    void recycle();


    /**
     * Get the name of the encoding handled by this filter.
     */
    ByteChunk getEncodingName();


    /**
     * Set the next buffer in the filter pipeline.
     */
    void setBuffer(InputBuffer buffer);


    /**
     * End the current request.
     *
     * @return 0 is the expected return value. A positive value indicates that
     * too many bytes were read. This method is allowed to use buffer.doRead
     * to consume extra bytes. The result of this method can't be negative (if
     * an error happens, an IOException should be thrown instead).
     */
    long end() throws IOException;


    /**
     * Amount of bytes still available in a buffer.
     */
    int available();

}
