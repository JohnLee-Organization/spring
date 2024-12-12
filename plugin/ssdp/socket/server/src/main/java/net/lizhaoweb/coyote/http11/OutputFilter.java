/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 16:51
 */
package net.lizhaoweb.coyote.http11;

import net.lizhaoweb.coyote.OutputBuffer;
import net.lizhaoweb.coyote.Response;
import net.lizhaoweb.mikefox.buf.ByteChunk;

import java.io.IOException;

/**
 * Output filter.
 * <p>
 * Created by jhon on 2024/6/7 16:51
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface OutputFilter extends OutputBuffer {


    /**
     * Write some bytes.
     *
     * @return number of bytes written by the filter
     */
    int doWrite(ByteChunk chunk, Response unused) throws IOException;


    /**
     * Some filters need additional parameters from the response. All the
     * necessary reading can occur in that method, as this method is called
     * after the response header processing is complete.
     */
    void setResponse(Response response);


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
    void setBuffer(OutputBuffer buffer);


    /**
     * End the current request. It is acceptable to write extra bytes using
     * buffer.doWrite during the execution of this method.
     *
     * @return Should return 0 unless the filter does some content length
     * delimitation, in which case the number is the amount of extra bytes or
     * missing bytes, which would indicate an error.
     * Note: It is recommended that extra bytes be swallowed by the filter.
     */
    long end() throws IOException;

}
