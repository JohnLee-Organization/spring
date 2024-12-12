/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.mikefox.http
 * @date : 2024-06-07
 * @time : 15:20
 */
package net.lizhaoweb.mikefox.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.buf.CharChunk;
import net.lizhaoweb.mikefox.buf.MessageBytes;
import net.lizhaoweb.mikefox.buf.UDecoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by jhon on 2024/6/7 15:20
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@NoArgsConstructor
@SuppressWarnings("unused")
public final class Parameters {

    // Transition: we'll use the same Hashtable( String->String[] )
    // for the beginning. When we are sure all accesses happen through
    // this class - we can switch to MultiMap
    private Hashtable<String, String[]> paramHashStringArray = new Hashtable<String, String[]>();
    private boolean didQueryParameters = false;

    MessageBytes queryMB;

    UDecoder urlDec;
    MessageBytes decodedQuery = MessageBytes.newInstance();

    @Getter
    String encoding = null;
    String queryStringEncoding = null;


    public void setQuery(MessageBytes queryMB) {
        this.queryMB = queryMB;
    }

    public void setEncoding(String s) {
        encoding = s;
        if (log.isDebugEnabled()) {
            log.debug("Set encoding to " + s);
        }
    }

    public void setQueryStringEncoding(String s) {
        queryStringEncoding = s;
        if (log.isDebugEnabled()) {
            log.debug("Set query string encoding to " + s);
        }
    }

    public void recycle() {
        paramHashStringArray.clear();
        didQueryParameters = false;
        encoding = null;
        decodedQuery.recycle();
    }

    // -------------------- Data access --------------------
    // Access to the current name/values, no side effect ( processing ).
    // You must explicitly call handleQueryParameters and the post methods.

    // This is the original data representation ( hash of String->String[])

    public void addParameterValues(String key, String[] newValues) {
        if (key == null) return;
        String[] values;
        if (paramHashStringArray.containsKey(key)) {
            String[] oldValues = paramHashStringArray.get(key);
            values = new String[oldValues.length + newValues.length];
            System.arraycopy(oldValues, 0, values, 0, oldValues.length);
            System.arraycopy(newValues, 0, values, oldValues.length, newValues.length);
        } else {
            values = newValues;
        }

        paramHashStringArray.put(key, values);
    }

    public String[] getParameterValues(String name) {
        handleQueryParameters();
        // no "facade"
        return paramHashStringArray.get(name);
    }

    public Enumeration<String> getParameterNames() {
        handleQueryParameters();
        return paramHashStringArray.keys();
    }

    // Shortcut.
    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        if (values != null) {
            if (values.length == 0) return "";
            return values[0];
        } else {
            return null;
        }
    }
    // -------------------- Processing --------------------

    /**
     * Process the query string into parameters
     */
    public void handleQueryParameters() {
        if (didQueryParameters) return;
        didQueryParameters = true;
        if (queryMB == null || queryMB.isNull()) return;
        if (log.isDebugEnabled()) {
            log.debug("Decoding query " + decodedQuery + " " + queryStringEncoding);
        }
        try {
            decodedQuery.duplicate(queryMB);
        } catch (IOException e) {
            // Can't happen, as decodedQuery can't overflow
            e.printStackTrace();
        }
        processParameters(decodedQuery, queryStringEncoding);
    }

    // incredibly inefficient data representation for parameters,
    // until we test the new one
    private void addParam(String key, String value) {
        if (key == null) return;
        String[] values;
        if (paramHashStringArray.containsKey(key)) {
            String[] oldValues = paramHashStringArray.get(key);
            values = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, values, 0, oldValues.length);
            values[oldValues.length] = value;
        } else {
            values = new String[1];
            values[0] = value;
        }
        paramHashStringArray.put(key, values);
    }

    public void setURLDecoder(UDecoder u) {
        urlDec = u;
    }

    // -------------------- Parameter parsing --------------------
    // we are called from a single thread - we can do it the hard way
    // if needed
    ByteChunk tmpName = new ByteChunk();
    ByteChunk tmpValue = new ByteChunk();
    private ByteChunk origName = new ByteChunk();
    private ByteChunk origValue = new ByteChunk();
    CharChunk tmpNameC = new CharChunk(1024);
    public static final String DEFAULT_ENCODING = "ISO-8859-1";

    public void processParameters(byte[] bytes, int start, int len) {
        processParameters(bytes, start, len, encoding);
    }

    public void processParameters(byte[] bytes, int start, int len, String enc) {
        int end = start + len;
        int pos = start;

        if (log.isDebugEnabled()) {
            try {
                log.debug("Bytes: " + new String(bytes, start, len, DEFAULT_ENCODING));
            } catch (UnsupportedEncodingException e) {
                // Should never happen...
                log.error("Unable to convert bytes", e);
            }
        }

        do {
            boolean noEq = false;
            int valStart = -1;
            int valEnd = -1;

            int nameStart = pos;
            int nameEnd = ByteChunk.indexOf(bytes, nameStart, end, '=');
            // Workaround for a&b&c encoding
            int nameEnd2 = ByteChunk.indexOf(bytes, nameStart, end, '&');
            if ((nameEnd2 != -1) && (nameEnd == -1 || nameEnd > nameEnd2)) {
                nameEnd = nameEnd2;
                noEq = true;
                valStart = nameEnd;
                valEnd = nameEnd;
                if (log.isDebugEnabled()) {
                    try {
                        log.debug("no equal " + nameStart + " " + nameEnd + " " + new String(bytes, nameStart, nameEnd - nameStart, DEFAULT_ENCODING));
                    } catch (UnsupportedEncodingException e) {
                        // Should never happen...
                        log.error("Unable to convert bytes", e);
                    }
                }
            }
            if (nameEnd == -1) nameEnd = end;
            if (!noEq) {
                valStart = (nameEnd < end) ? nameEnd + 1 : end;
                valEnd = ByteChunk.indexOf(bytes, valStart, end, '&');
                if (valEnd == -1) valEnd = Math.max(valStart, end);
            }
            pos = valEnd + 1;
            if (nameEnd <= nameStart) {
                StringBuilder msg = new StringBuilder("Parameters: Invalid chunk ");
                // No name eg ...&=xx&... will trigger this
                if (valEnd >= nameStart) {
                    msg.append('\'');
                    try {
                        msg.append(new String(bytes, nameStart, valEnd - nameStart, DEFAULT_ENCODING));
                    } catch (UnsupportedEncodingException e) {
                        // Should never happen...
                        log.error("Unable to convert bytes", e);
                    }
                    msg.append("' ");
                }
                msg.append("ignored.");
                log.warn(msg.toString());
                continue;
                // invalid chunk - it's better to ignore
            }
            tmpName.setBytes(bytes, nameStart, nameEnd - nameStart);
            tmpValue.setBytes(bytes, valStart, valEnd - valStart);

            // Take copies as if anything goes wrong originals will be
            // corrupted. This means original values can be logged.
            // For performance - only done for debug
            if (log.isDebugEnabled()) {
                try {
                    origName.append(bytes, nameStart, nameEnd - nameStart);
                    origValue.append(bytes, valStart, valEnd - valStart);
                } catch (IOException ioe) {
                    // Should never happen...
                    log.error("Error copying parameters", ioe);
                }
            }
            try {
                addParam(urlDecode(tmpName, enc), urlDecode(tmpValue, enc));
            } catch (IOException e) {
                StringBuilder msg = new StringBuilder("Parameters: Character decoding failed.");
                msg.append(" Parameter '");
                if (log.isDebugEnabled()) {
                    msg.append(origName.toString());
                    msg.append("' with value '");
                    msg.append(origValue.toString());
                    msg.append("' has been ignored.");
                    log.debug(msg.toString(), e);
                } else {
                    msg.append(tmpName.toString());
                    msg.append("' with value '");
                    msg.append(tmpValue.toString());
                    msg.append("' has been ignored. Note that the name and ");
                    msg.append("value quoted here may be corrupted due to ");
                    msg.append("the failed decoding. Use debug level logging ");
                    msg.append("to see the original, non-corrupted values.");
                    log.warn(msg.toString());
                }
            }
            tmpName.recycle();
            tmpValue.recycle();
            // Only recycle copies if we used them
            if (log.isDebugEnabled()) {
                origName.recycle();
                origValue.recycle();
            }
        } while (pos < end);
    }

    private String urlDecode(ByteChunk bc, String enc) throws IOException {
        if (urlDec == null) {
            urlDec = new UDecoder();
        }
        urlDec.convert(bc);
        String result = null;
        if (enc != null) {
            bc.setEncoding(enc);
            result = bc.toString();
        } else {
            CharChunk cc = tmpNameC;
            int length = bc.getLength();
            cc.allocate(length, -1);
            // Default encoding: fast conversion
            byte[] bbuf = bc.getBuffer();
            char[] cbuf = cc.getBuffer();
            int start = bc.getStart();
            for (int i = 0; i < length; i++) {
                cbuf[i] = (char) (bbuf[i + start] & 0xff);
            }
            cc.setChars(cbuf, 0, length);
            result = cc.toString();
            cc.recycle();
        }
        return result;
    }

    public void processParameters(MessageBytes data, String encoding) {
        if (data == null || data.isNull() || data.getLength() <= 0) return;
        if (data.getType() != MessageBytes.T_BYTES) {
            data.toBytes();
        }
        ByteChunk bc = data.getByteChunk();
        processParameters(bc.getBytes(), bc.getOffset(), bc.getLength(), encoding);
    }

    /**
     * Debug purpose
     */
    public String paramsAsString() {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> en = paramHashStringArray.keys();
        while (en.hasMoreElements()) {
            String k = en.nextElement();
            sb.append(k).append("=");
            String[] v = paramHashStringArray.get(k);
            for (String s : v) sb.append(s).append(",");
            sb.append("\n");
        }
        return sb.toString();
    }

}
