/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.buf
 * @date : 2024-06-07
 * @time : 15:05
 */
package net.lizhaoweb.mikefox.buf;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.BitSet;

/**
 * Efficient implementation for encoders.
 * This class is not thread safe - you need one encoder per thread.
 * The encoder will save and recycle the internal objects, avoiding
 * garbage.
 * <p>
 * You can add extra characters that you want preserved, for example
 * while encoding a URL you can add "/".
 * <p>
 * Created by jhon on 2024/6/7 15:05
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public final class UEncoder {

    // Not static - the set may differ ( it's better than adding
    // an extra check for "/", "+", etc
    private BitSet safeChars = null;
    private C2BConverter c2b = null;
    private ByteChunk bb = null;

    @Setter
    private String encoding = "UTF8";

    public UEncoder() {
        initSafeChars();
    }

    public void addSafeCharacter(char c) {
        safeChars.set(c);
    }


    /**
     * URL Encode string, using a specified encoding.
     *
     * @param buf The writer
     * @param s   string to be encoded
     * @throws IOException If an I/O error occurs
     */
    public void urlEncode(Writer buf, String s) throws IOException {
        if (c2b == null) {
            bb = new ByteChunk(16); // small enough.
            c2b = new C2BConverter(bb, encoding);
        }

        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i);
            if (safeChars.get(c)) {
                if (log.isDebugEnabled()) log.debug("Encoder: Safe: " + (char) c);
                buf.write((char) c);
            } else {
                if (log.isDebugEnabled()) log.debug("Encoder: Unsafe:  " + (char) c);
                c2b.convert((char) c);

                // "surrogate" - UTF is _not_ 16 bit, but 21 !!!!
                // ( while UCS is 31 ). Amazing...
                if (c >= 0xD800 && c <= 0xDBFF) {
                    if ((i + 1) < s.length()) {
                        int d = s.charAt(i + 1);
                        if (d >= 0xDC00 && d <= 0xDFFF) {
                            if (log.isDebugEnabled()) log.debug("Encoder: Unsafe:  " + c);
                            c2b.convert((char) d);
                            i++;
                        }
                    }
                }
                c2b.flushBuffer();
                urlEncode(buf, bb.getBuffer(), bb.getOffset(), bb.getLength());
                bb.recycle();
            }
        }
    }

    public void urlEncode(Writer buf, byte[] bytes, int off, int len) throws IOException {
        for (int j = off; j < len; j++) {
            buf.write('%');
            char ch = Character.forDigit((bytes[j] >> 4) & 0xF, 16);
            if (log.isDebugEnabled()) log.debug("Encoder: Encode:  " + ch);
            buf.write(ch);
            ch = Character.forDigit(bytes[j] & 0xF, 16);
            if (log.isDebugEnabled()) log.debug("Encoder: Encode:  " + ch);
            buf.write(ch);
        }
    }

    /**
     * Utility function to re-encode the URL.
     * Still has problems with charset, since UEncoder mostly
     * ignores it.
     */
    public String encodeURL(String uri) {
        String outUri = null;
        try {
            // XXX optimize - recycle, etc
            CharArrayWriter out = new CharArrayWriter();
            urlEncode(out, uri);
            outUri = out.toString();
        } catch (IOException ignored) {
            // ignore
        }
        return outUri;
    }


    // -------------------- Internal implementation --------------------

    private void initSafeChars() {
        safeChars = new BitSet(128);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            safeChars.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            safeChars.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            safeChars.set(i);
        }
        //safe
        safeChars.set('$');
        safeChars.set('-');
        safeChars.set('_');
        safeChars.set('.');

        // Dangerous: someone may treat this as " "
        // RFC1738 does allow it, it's not reserved
        //    safeChars.set('+');
        //extra
        safeChars.set('!');
        safeChars.set('*');
        safeChars.set('\'');
        safeChars.set('(');
        safeChars.set(')');
        safeChars.set(',');
    }

}
