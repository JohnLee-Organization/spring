/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.buf
 * @date : 2024-06-11
 * @time : 09:21
 */
package net.lizhaoweb.mikefox.buf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Efficient conversion of bytes  to character .
 * <p>
 * This uses the standard JDK mechanism - a reader - but provides mechanisms
 * to recycle all the objects that are used. It is compatible with JDK1.1
 * and up,
 * ( nio is better, but it's not available even in 1.2 or 1.3 )
 * <p>
 * Not used in the current code, the performance gain is not very big
 * in the current case ( since String is created anyway ), but it will
 * be used in a later version or after the remaining optimizations.
 * <p>
 * Created by jhon on 2024/6/11 9:21
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class B2CConverter {

    private IntermediateInputStream iis;
    private ReadConvertor conv;
    private String encoding;

    /**
     * Create a converter, with bytes going to a byte buffer
     */
    public B2CConverter(String encoding) throws IOException {
        this.encoding = encoding;
        reset();
    }


    /**
     * Reset the internal state, empty the buffers.
     * The encoding remain in effect, the internal buffers remain allocated.
     */
    public void recycle() {
        conv.recycle();
    }

    static final int BUFFER_SIZE = 8192;
    char[] result = new char[BUFFER_SIZE];

    public void convert(ByteChunk bb, CharChunk cb, int limit) throws IOException {
        iis.setByteChunk(bb);
        try {
            // read from the reader
            int bbLengthBeforeRead = 0;
            while (limit > 0) { // conv.ready() ) {
                int size = Math.min(limit, BUFFER_SIZE);
                bbLengthBeforeRead = bb.getLength();
                int cnt = conv.read(result, 0, size);
                if (cnt <= 0) {
                    // End of stream ! - we may be in a bad state
                    if (log.isDebugEnabled()) log.debug("B2CConverter: EOF");
                    return;
                }
                if (log.isDebugEnabled()) log.debug("B2CConverter: Converted: " + new String(result, 0, cnt));
                cb.append(result, 0, cnt);
                limit = limit - (bbLengthBeforeRead - bb.getLength());
            }
        } catch (IOException ex) {
            if (log.isDebugEnabled()) log.debug("B2CConverter: Reseting the converter " + ex.toString());
            reset();
            throw ex;
        }
    }


    public void reset() throws IOException {
        // destroy the reader/iis
        iis = new IntermediateInputStream();
        conv = new ReadConvertor(iis, encoding);
    }

}

// -------------------- Private implementation --------------------


/**
 *
 */
final class ReadConvertor extends InputStreamReader {

    /**
     * Create a converter.
     */
    public ReadConvertor(IntermediateInputStream in, String enc) throws UnsupportedEncodingException {
        super(in, enc);
    }

    /**
     * Overridden - will do nothing but reset internal state.
     */
    @Override
    public final void close() throws IOException {
        // NOTHING
        // Calling super.close() would reset out and cb.
    }

    @Override
    public final int read(char[] cbuf, int off, int len) throws IOException {
        // will do the conversion and call write on the output stream
        return super.read(cbuf, off, len);
    }

    /**
     * Reset the buffer
     */
    public final void recycle() {
        try {
            // Must clear super's buffer.
            while (ready()) {
                // InputStreamReader#skip(long) will allocate buffer to skip.
                read();
            }
        } catch (IOException ioe) {
            //ignore
        }
    }

}


/**
 * Special output stream where close() is overridden, so super.close()
 * is never called.
 * <p>
 * This allows recycling. It can also be disabled, so callbacks will
 * not be called if recycling the converter and if data was not flushed.
 */
@NoArgsConstructor
final class IntermediateInputStream extends InputStream {
    ByteChunk bc = null;

    @Override
    public final void close() throws IOException {
        // shouldn't be called - we filter it out in writer
        throw new IOException("close() called - shouldn't happen ");
    }

    @Override
    public final int read(byte[] cbuf, int off, int len) throws IOException {
        return bc.substract(cbuf, off, len);
    }

    @Override
    public final int read() throws IOException {
        return bc.substract();
    }

    // -------------------- Internal methods --------------------


    void setByteChunk(ByteChunk mb) {
        bc = mb;
    }

}
