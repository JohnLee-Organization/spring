/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.buf
 * @date : 2024-06-07
 * @time : 15:09
 */
package net.lizhaoweb.mikefox.buf;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Efficient conversion of character to bytes.
 * <p>
 * This uses the standard JDK mechanism - a writer - but provides mechanisms
 * to recycle all the objects that are used. It is compatible with JDK1.1 and up,
 * ( nio is better, but it's not available even in 1.2 or 1.3 )
 * <p>
 * Created by jhon on 2024/6/7 15:09
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public final class C2BConverter {

    private IntermediateOutputStream ios;
    private WriteConvertor conv;
    private ByteChunk bb;
    private String enc;

    /**
     * Create a converter, with bytes going to a byte buffer
     */
    public C2BConverter(ByteChunk output, String encoding) throws IOException {
        this.bb = output;
        ios = new IntermediateOutputStream(output);
        conv = new WriteConvertor(ios, encoding);
        this.enc = encoding;
    }

    /**
     * Create a converter
     */
    public C2BConverter(String encoding) throws IOException {
        this(new ByteChunk(1024), encoding);
    }

    public ByteChunk getByteChunk() {
        return bb;
    }

    public String getEncoding() {
        return enc;
    }

    public void setByteChunk(ByteChunk bb) {
        this.bb = bb;
        ios.setByteChunk(bb);
    }

    /**
     * Reset the internal state, empty the buffers.
     * The encoding remain in effect, the internal buffers remain allocated.
     */
    public final void recycle() {
        conv.recycle();
        bb.recycle();
    }

    /**
     * Generate the bytes using the specified encoding
     */
    public final void convert(char[] c, int off, int len) throws IOException {
        conv.write(c, off, len);
    }

    /**
     * Generate the bytes using the specified encoding
     */
    public final void convert(String s, int off, int len) throws IOException {
        conv.write(s, off, len);
    }

    /**
     * Generate the bytes using the specified encoding
     */
    public final void convert(String s) throws IOException {
        conv.write(s);
    }

    /**
     * Generate the bytes using the specified encoding
     */
    public final void convert(char c) throws IOException {
        conv.write(c);
    }

    /**
     * Convert a message bytes chars to bytes
     */
    public final void convert(MessageBytes mb) throws IOException {
        int type = mb.getType();
        if (type == MessageBytes.T_BYTES) return;
        ByteChunk orig = bb;
        setByteChunk(mb.getByteChunk());
        bb.recycle();
        bb.allocate(32, -1);
        if (type == MessageBytes.T_STR) {
            convert(mb.getString());
            // System.out.println("XXX Converting " + mb.getString() );
        } else if (type == MessageBytes.T_CHARS) {
            CharChunk charC = mb.getCharChunk();
            convert(charC.getBuffer(), charC.getOffset(), charC.getLength());
            //System.out.println("XXX Converting " + mb.getCharChunk() );
        } else {
            if (log.isDebugEnabled()) log.debug("XXX unknowon type " + type);
        }
        flushBuffer();
        //System.out.println("C2B: XXX " + bb.getBuffer() + bb.getLength());
        setByteChunk(orig);
    }

    /**
     * Flush any internal buffers into the ByteOutput or the internal
     * byte[]
     */
    public final void flushBuffer() throws IOException {
        conv.flush();
    }

}

// -------------------- Private implementation --------------------


/**
 * Special writer class, where close() is overridden. The default implementation
 * would set byteOutputter to null, and the writer can't be recycled.
 * <p>
 * Note that the flush method will empty the internal buffers _and_ call
 * flush on the output stream - that's why we use an intermediary output stream
 * that overrides flush(). The idea is to  have full control: flushing the
 * char->byte converter should be independent of flushing the OutputStream.
 * <p>
 * When a WriteConverter is created, it'll allocate one or 2 byte buffers,
 * with a 8k size that can't be changed ( at least in JDK1.1 -> 1.4 ). It would
 * also allocate a ByteOutputter or equivalent - again some internal buffers.
 * <p>
 * It is essential to keep  this object around and reuse it. You can use either
 * pools or per thread data - but given that in most cases a converter will be
 * needed for every thread and most of the time only 1 ( or 2 ) encodings will
 * be used, it is far better to keep it per thread and eliminate the pool
 * overhead too.
 */
final class WriteConvertor extends OutputStreamWriter {
    // stream with flush() and close(). overridden.
    private IntermediateOutputStream ios;

    // Has a private, internal byte[8192]

    /**
     * Create a converter.
     */
    public WriteConvertor(IntermediateOutputStream out, String enc) throws UnsupportedEncodingException {
        super(out, enc);
        ios = out;
    }

    /**
     * Overridden - will do nothing but reset internal state.
     */
    @Override
    public final void close() throws IOException {
        // NOTHING
        // Calling super.close() would reset out and cb.
    }

    /**
     * Flush the characters only
     */
    @Override
    public final void flush() throws IOException {
        // Will flushBuffer and out()
        // flushBuffer put any remaining chars in the byte[]
        super.flush();
    }

    @Override
    public final void write(char[] cbuf, int off, int len) throws IOException {
        // will do the conversion and call write on the output stream
        super.write(cbuf, off, len);
    }

    /**
     * Reset the buffer
     */
    public final void recycle() {
        ios.disable();
        try {
            //	    System.out.println("Reseting writer");
            flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ios.enable();
    }

}


/**
 * Special output stream where close() is overridden, so super.close()
 * is never called.
 * <p>
 * This allows recycling. It can also be disabled, so callbacks will
 * not be called if recycling the converter and if data was not flushed.
 */
final class IntermediateOutputStream extends OutputStream {
    private ByteChunk tbuff;
    private boolean enabled = true;

    public IntermediateOutputStream(ByteChunk tbuff) {
        this.tbuff = tbuff;
    }

    @Override
    public final void close() throws IOException {
        // shouldn't be called - we filter it out in writer
        throw new IOException("close() called - shouldn't happen ");
    }

    @Override
    public final void flush() throws IOException {
        // nothing - write will go directly to the buffer,
        // we don't keep any state
    }

    @Override
    public final void write(byte[] cbuf, int off, int len) throws IOException {
        // will do the conversion and call write on the output stream
        if (enabled) {
            tbuff.append(cbuf, off, len);
        }
    }

    @Override
    public final void write(int i) throws IOException {
        throw new IOException("write( int ) called - shouldn't happen ");
    }

    // -------------------- Internal methods --------------------

    void setByteChunk(ByteChunk bb) {
        tbuff = bb;
    }

    /**
     * Temporary disable - this is used to recycle the converter without
     * generating an output if the buffers were not flushed
     */
    final void disable() {
        enabled = false;
    }

    /**
     * Reenable - used to recycle the converter
     */
    final void enable() {
        enabled = true;
    }

}
