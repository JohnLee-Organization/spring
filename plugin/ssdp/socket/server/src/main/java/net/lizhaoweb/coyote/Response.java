/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:13
 */
package net.lizhaoweb.coyote;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.http.MimeHeaders;

import java.io.IOException;
import java.util.Locale;

/**
 * Response object.
 * <p>
 * Created by jhon on 2024/6/7 14:13
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@NoArgsConstructor
@SuppressWarnings("unused")
public final class Response {


    // ----------------------------------------------------- Class Variables

    /**
     * Default locale as mandated by the spec.
     */
    private static Locale DEFAULT_LOCALE = Locale.getDefault();


    // ----------------------------------------------------- Instance Variables

    /**
     * Status code.
     */
    @Setter
    @Getter
    private int status = 200;


    /**
     * Status message.
     */
    @Setter
    @Getter
    private String message = null;


    /**
     * Response headers.
     */
    private MimeHeaders headers = new MimeHeaders();


    /**
     * Associated output buffer.
     */
    @Setter
    @Getter
    private OutputBuffer outputBuffer;


    /**
     * Notes.
     */
    private Object[] notes = new Object[Constants.MAX_NOTES];


    /**
     * Committed flag.
     */
    private boolean commited = false;


    /**
     * Action hook.
     */
    @Setter
    @Getter
    public ActionHook hook;


    /**
     * HTTP specific fields.
     */
    private String contentType = null;
    @Getter
    private String contentLanguage = null;
    @Getter
    private String characterEncoding = Constants.DEFAULT_CHARACTER_ENCODING;
    private long contentLength = -1;
    @Getter
    private Locale locale = DEFAULT_LOCALE;

    // General informations
    @Setter
    @Getter
    private long bytesWritten = 0;

    /**
     * Holds request error exception.
     */
    @Setter
    @Getter
    private Exception errorException = null;

    /**
     * Has the charset been explicitly set.
     */
    private boolean charsetSet = false;

    /**
     * Request error URI.
     */
    @Setter
    @Getter
    private String errorURI = null;

    private Request req;

    // ------------------------------------------------------------- Properties

    public Request getRequest() {
        return req;
    }

    public void setRequest(Request req) {
        this.req = req;
    }


    public MimeHeaders getMimeHeaders() {
        return headers;
    }


    // -------------------- Per-Response "notes" --------------------


    public final void setNote(int pos, Object value) {
        notes[pos] = value;
    }


    public final Object getNote(int pos) {
        return notes[pos];
    }


    // -------------------- Actions --------------------


    public void action(ActionCode actionCode, Object param) {
        if (hook != null) {
            if (param == null) hook.action(actionCode, this);
            else hook.action(actionCode, param);
        }
    }


    // -------------------- State --------------------


    public boolean isCommitted() {
        return commited;
    }


    public void setCommitted(boolean v) {
        this.commited = v;
    }


    // -----------------Error State --------------------


    public boolean isExceptionPresent() {
        return (errorException != null);
    }


    // -------------------- Methods --------------------


    public void reset() throws IllegalStateException {
        // Reset the headers only if this is the main request,
        // not for included
        contentType = null;
        locale = DEFAULT_LOCALE;
        contentLanguage = null;
        characterEncoding = Constants.DEFAULT_CHARACTER_ENCODING;
        contentLength = -1;
        charsetSet = false;
        status = 200;
        message = null;
        headers.clear();

        // Force the PrintWriter to flush its data to the output
        // stream before resetting the output stream
        //
        // Reset the stream
        if (commited) {
            //String msg = sm.getString("servletOutputStreamImpl.reset.ise");
            throw new IllegalStateException();
        }
        action(ActionCode.ACTION_RESET, this);
    }


    public void finish() throws IOException {
        action(ActionCode.ACTION_CLOSE, this);
    }


    public void acknowledge() throws IOException {
        action(ActionCode.ACTION_ACK, this);
    }


    // -------------------- Headers --------------------

    /**
     * Warning: This method always returns <code>false<code> for Content-Type
     * and Content-Length.
     */
    public boolean containsHeader(String name) {
        return headers.getHeader(name) != null;
    }


    public void setHeader(String name, String value) {
        char cc = name.charAt(0);
        if (cc == 'C' || cc == 'c') {
            if (checkSpecialHeader(name, value)) return;
        }
        headers.setValue(name).setString(value);
    }


    public void addHeader(String name, String value) {
        char cc = name.charAt(0);
        if (cc == 'C' || cc == 'c') {
            if (checkSpecialHeader(name, value)) return;
        }
        headers.addValue(name).setString(value);
    }


    /**
     * Set internal fields for special header names.
     * Called from set/addHeader.
     * Return true if the header is special, no need to set the header.
     */
    private boolean checkSpecialHeader(String name, String value) {
        // XXX Eliminate redundant fields !!!
        // ( both header and in special fields )
        if (name.equalsIgnoreCase("Content-Type")) {
            setContentType(value);
            return true;
        }
        if (name.equalsIgnoreCase("Content-Length")) {
            try {
                long cL = Long.parseLong(value);
                setContentLength(cL);
                return true;
            } catch (NumberFormatException ex) {
                // Do nothing - the spec doesn't have any "throws"
                // and the user might know what he's doing
                return false;
            }
        }
        return false;
    }


    /**
     * Signal that we're done with the headers, and body will follow.
     * Any implementation needs to notify ContextManager, to allow
     * interceptors to fix headers.
     */
    public void sendHeaders() throws IOException {
        action(ActionCode.ACTION_COMMIT, this);
        commited = true;
    }


    // -------------------- I18N --------------------


    /**
     * Called explicitly by user to set the Content-Language and
     * the default encoding
     */
    public void setLocale(Locale locale) {
        if (locale == null) {
            return;  // throw an exception?
        }

        // Save the locale for use by getLocale()
        this.locale = locale;

        // Set the contentLanguage for header output
        contentLanguage = locale.getLanguage();
        if ((contentLanguage != null) && (contentLanguage.length() > 0)) {
            String country = locale.getCountry();
            StringBuilder value = new StringBuilder(contentLanguage);
            if ((country != null) && (country.length() > 0)) {
                value.append('-');
                value.append(country);
            }
            contentLanguage = value.toString();
        }
    }

    /*
     * Overrides the name of the character encoding used in the body
     * of the response. This method must be called prior to writing output
     * using getWriter().
     *
     * @param charset String containing the name of the character encoding.
     */
    public void setCharacterEncoding(String charset) {
        if (isCommitted()) return;
        if (charset == null) return;
        characterEncoding = charset;
        charsetSet = true;
    }

    /**
     * Sets the content type.
     * <p>
     * This method must preserve any response charset that may already have
     * been set via a call to response.setContentType(), response.setLocale(),
     * or response.setCharacterEncoding().
     *
     * @param type the content type
     */
    public void setContentType(String type) {
        int semicolonIndex = -1;
        if (type == null) {
            this.contentType = null;
            return;
        }

        /*
         * Remove the charset param (if any) from the Content-Type, and use it
         * to set the response encoding.
         * The most recent response encoding setting will be appended to the
         * response's Content-Type (as its charset param) by getContentType();
         */
        boolean hasCharset = false;
        int len = type.length();
        int index = type.indexOf(';');
        while (index != -1) {
            semicolonIndex = index;
            index++;
            while (index < len && Character.isSpaceChar(type.charAt(index))) {
                index++;
            }
            if (index + 8 < len && type.charAt(index) == 'c' && type.charAt(index + 1) == 'h' && type.charAt(index + 2) == 'a' && type.charAt(index + 3) == 'r' && type.charAt(index + 4) == 's' && type.charAt(index + 5) == 'e' && type.charAt(index + 6) == 't' && type.charAt(index + 7) == '=') {
                hasCharset = true;
                break;
            }
            index = type.indexOf(';', index);
        }

        if (!hasCharset) {
            this.contentType = type;
            return;
        }

        this.contentType = type.substring(0, semicolonIndex);
        String tail = type.substring(index + 8);
        int nextParam = tail.indexOf(';');
        String charsetValue = null;
        if (nextParam != -1) {
            this.contentType += tail.substring(nextParam);
            charsetValue = tail.substring(0, nextParam);
        } else {
            charsetValue = tail;
        }

        assert false;
        // The charset value may be quoted, but must not contain any quotes.
        if (charsetValue != null && charsetValue.length() > 0) {
            charsetSet = true;
            charsetValue = charsetValue.replace('"', ' ');
            this.characterEncoding = charsetValue.trim();
        }
    }

    public String getContentType() {
        String ret = contentType;
        if (ret != null && characterEncoding != null && charsetSet) {
            ret = ret + ";charset=" + characterEncoding;
        }
        return ret;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getContentLength() {
        long length = getContentLengthLong();
        if (length < Integer.MAX_VALUE) {
            return (int) length;
        }
        return -1;
    }

    public long getContentLengthLong() {
        return contentLength;
    }


    /**
     * Write a chunk of bytes.
     */
    public void doWrite(ByteChunk chunk/*byte buffer[], int pos, int count*/) throws IOException {
        outputBuffer.doWrite(chunk, this);
        bytesWritten += chunk.getLength();
    }

    // --------------------

    public void recycle() {
        contentType = null;
        contentLanguage = null;
        locale = DEFAULT_LOCALE;
        characterEncoding = Constants.DEFAULT_CHARACTER_ENCODING;
        charsetSet = false;
        contentLength = -1;
        status = 200;
        message = null;
        commited = false;
        errorException = null;
        errorURI = null;
        headers.clear();

        // update counters
        bytesWritten = 0;
    }

}
