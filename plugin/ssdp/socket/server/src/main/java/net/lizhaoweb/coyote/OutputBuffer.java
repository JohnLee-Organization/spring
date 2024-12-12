/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:52
 */
package net.lizhaoweb.coyote;

import net.lizhaoweb.mikefox.buf.ByteChunk;

import java.io.IOException;

/**
 * Output buffer.
 * <p>
 * This class is used internally by the protocol implementation. All writes from higher level code should happen
 * via Resonse.doWrite().
 * <p>
 * Created by jhon on 2024/6/7 14:52
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface OutputBuffer {


    /**
     * Write the response. The caller ( tomcat ) owns the chunks.
     *
     * @param chunk    data to write
     * @param response used to allow buffers that can be shared by multiple responses.
     * @return int
     * @throws IOException IOException
     */
    int doWrite(ByteChunk chunk, Response response) throws IOException;

}
