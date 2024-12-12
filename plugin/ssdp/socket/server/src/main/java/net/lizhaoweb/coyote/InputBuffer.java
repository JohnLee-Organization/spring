/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:50
 */
package net.lizhaoweb.coyote;

import net.lizhaoweb.mikefox.buf.ByteChunk;

import java.io.IOException;

/**
 * Input buffer.
 * <p>
 * This class is used only in the protocol implementation. All reading from tomcat ( or adapter ) should be done
 * using Request.doRead().
 * <p>
 * Created by jhon on 2024/6/7 14:50
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface InputBuffer {


    /**
     * Return from the input stream.
     * IMPORTANT: the current model assumes that the protocol will 'own' the
     * buffer and return a pointer to it in ByteChunk ( i.e. the param will
     * have chunk.getBytes()==null before call, and the result after the call ).
     */
    int doRead(ByteChunk chunk, Request request) throws IOException;

}
