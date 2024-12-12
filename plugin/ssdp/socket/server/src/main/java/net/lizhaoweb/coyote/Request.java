/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:11
 */
package net.lizhaoweb.coyote;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.buf.MessageBytes;
import net.lizhaoweb.mikefox.buf.UDecoder;
import net.lizhaoweb.mikefox.http.ContentType;
import net.lizhaoweb.mikefox.http.MimeHeaders;
import net.lizhaoweb.mikefox.http.Parameters;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is a low-level, efficient representation of a server request. Most
 * fields are GC-free, expensive operations are delayed until the  user code
 * needs the information.
 * <p>
 * Processing is delegated to modules, using a hook mechanism.
 * <p>
 * This class is not intended for user code - it is used internally by tomcat
 * for processing the request in the most efficient way. Users ( servlets ) can
 * access the information using a facade, which provides the high-level view
 * of the request.
 * <p>
 * For lazy evaluation, the request uses the getInfo() hook. The following ids
 * are defined:
 * <ul>
 *  <li>req.encoding - returns the request encoding
 *  <li>req.attribute - returns a module-specific attribute ( like SSL keys, etc ).
 * </ul>
 * <p>
 * Tomcat defines a number of attributes:
 * <ul>
 *   <li>"org.apache.tomcat.request" - allows access to the low-level
 *       request object in trusted applications
 * </ul>
 *
 * <p>
 * Created by jhon on 2024/6/7 14:11
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public final class Request {


    // ----------------------------------------------------------- Constructors


    public Request() {
        parameters.setQuery(queryMB);
        parameters.setURLDecoder(urlDecoder);
    }


    // ----------------------------------------------------- Instance Variables


    @Setter
    @Getter
    private int serverPort = -1;
    private MessageBytes serverNameMB = MessageBytes.newInstance();

    @Setter
    @Getter
    private int remotePort;
    @Setter
    @Getter
    private int localPort;

    private MessageBytes schemeMB = MessageBytes.newInstance();

    private MessageBytes methodMB = MessageBytes.newInstance();
    private MessageBytes unparsedURIMB = MessageBytes.newInstance();
    private MessageBytes uriMB = MessageBytes.newInstance();
    private MessageBytes decodedUriMB = MessageBytes.newInstance();
    private MessageBytes queryMB = MessageBytes.newInstance();
    private MessageBytes protoMB = MessageBytes.newInstance();

    // remote address/host
    private MessageBytes remoteAddrMB = MessageBytes.newInstance();
    private MessageBytes localNameMB = MessageBytes.newInstance();
    private MessageBytes remoteHostMB = MessageBytes.newInstance();
    private MessageBytes localAddrMB = MessageBytes.newInstance();

    private MimeHeaders headers = new MimeHeaders();

    private MessageBytes instanceId = MessageBytes.newInstance();

    /**
     * Notes.
     */
    private Object[] notes = new Object[Constants.MAX_NOTES];


    /**
     * Associated input buffer.
     */
    @Setter
    @Getter
    private InputBuffer inputBuffer = null;


    /**
     * URL decoder.
     */
    private UDecoder urlDecoder = new UDecoder();


    /**
     * HTTP specific fields. (remove them ?)
     */
    @Setter
    private long contentLength = -1;
    private MessageBytes contentTypeMB = null;
    private String charEncoding = null;
    private Parameters parameters = new Parameters();

    private MessageBytes remoteUser = MessageBytes.newInstance();
    private MessageBytes authType = MessageBytes.newInstance();
    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    @Getter
    private Response response;
    private ActionHook hook;

    @Setter
    @Getter
    private int bytesRead = 0;
    // Time of the request - useful to avoid repeated calls to System.currentTime
    @Setter
    @Getter
    private long startTime = 0L;
    @Setter
    @Getter
    private int available = 0;

    private RequestInfo reqProcessorMX = new RequestInfo(this);
    // ------------------------------------------------------------- Properties


    /**
     * Get the instance id (or JVM route). Currently Ajp is sending it with each
     * request. In future this should be fixed, and sent only once ( or
     * 'negotiated' at config time so both tomcat and apache share the same name.
     *
     * @return the instance id
     */
    public MessageBytes instanceId() {
        return instanceId;
    }


    public MimeHeaders getMimeHeaders() {
        return headers;
    }


    public UDecoder getURLDecoder() {
        return urlDecoder;
    }

    // -------------------- Request data --------------------


    public MessageBytes scheme() {
        return schemeMB;
    }

    public MessageBytes method() {
        return methodMB;
    }

    public MessageBytes unparsedURI() {
        return unparsedURIMB;
    }

    public MessageBytes requestURI() {
        return uriMB;
    }

    public MessageBytes decodedURI() {
        return decodedUriMB;
    }

    public MessageBytes query() {
        return queryMB;
    }

    public MessageBytes queryString() {
        return queryMB;
    }

    public MessageBytes protocol() {
        return protoMB;
    }

    /**
     * Return the buffer holding the server name, if
     * any. Use isNull() to check if there is no value
     * set.
     * This is the "virtual host", derived from the
     * Host: header.
     */
    public MessageBytes serverName() {
        return serverNameMB;
    }

    public MessageBytes remoteAddr() {
        return remoteAddrMB;
    }

    public MessageBytes remoteHost() {
        return remoteHostMB;
    }

    public MessageBytes localName() {
        return localNameMB;
    }

    public MessageBytes localAddr() {
        return localAddrMB;
    }


    // -------------------- encoding/type --------------------


    /**
     * Get the character encoding used for this request.
     */
    public String getCharacterEncoding() {
        if (charEncoding != null) return charEncoding;
        charEncoding = ContentType.getCharsetFromContentType(getContentType());
        return charEncoding;
    }


    public void setCharacterEncoding(String enc) {
        this.charEncoding = enc;
    }


    public int getContentLength() {
        long length = getContentLengthLong();
        if (length < Integer.MAX_VALUE) {
            return (int) length;
        }
        return -1;
    }

    public long getContentLengthLong() {
        if (contentLength > -1) return contentLength;
        MessageBytes clB = headers.getUniqueValue("content-length");
        contentLength = (clB == null || clB.isNull()) ? -1 : clB.getLong();
        return contentLength;
    }

    public String getContentType() {
        contentType();
        if ((contentTypeMB == null) || contentTypeMB.isNull()) return null;
        return contentTypeMB.toString();
    }


    public void setContentType(String type) {
        contentTypeMB.setString(type);
    }


    public MessageBytes contentType() {
        if (contentTypeMB == null) contentTypeMB = headers.getValue("content-type");
        return contentTypeMB;
    }


    public void setContentType(MessageBytes mb) {
        contentTypeMB = mb;
    }


    public String getHeader(String name) {
        return headers.getHeader(name);
    }

    // -------------------- Associated response --------------------

    public void setResponse(Response response) {
        this.response = response;
        response.setRequest(this);
    }

    public void action(ActionCode actionCode, Object param) {
        if (hook == null && response != null) hook = response.getHook();
        if (hook != null) {
            if (param == null) hook.action(actionCode, this);
            else hook.action(actionCode, param);
        }
    }


    // -------------------- Parameters --------------------


    public Parameters getParameters() {
        return parameters;
    }


    // -------------------- Other attributes --------------------
    // We can use notes for most - need to discuss what is of general interest

    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public MessageBytes getRemoteUser() {
        return remoteUser;
    }

    public MessageBytes getAuthType() {
        return authType;
    }

    // -------------------- Input Buffer --------------------


    /**
     * Read data from the input buffer and put it into a byte chunk.
     * <p>
     * The buffer is owned by the protocol implementation - it will be reused on the next read.
     * The Adapter must either process the data in place or copy it to a separate buffer if it needs
     * to hold it. In most cases this is done during byte->char conversions or via InputStream. Unlike
     * InputStream, this interface allows the app to process data in place, without copy.
     */
    public int doRead(ByteChunk chunk) throws IOException {
        int n = inputBuffer.doRead(chunk, this);
        if (n > 0) {
            bytesRead += n;
        }
        return n;
    }


    // -------------------- debug --------------------

    @Override
    public String toString() {
        return "R( " + requestURI().toString() + ")";
    }

    // -------------------- Per-Request "notes" --------------------


    /**
     * Used to store private data. Thread data could be used instead - but
     * if you have the req, getting/setting a note is just a array access, may
     * be faster than ThreadLocal for very frequent operations.
     * <p>
     * Example use:
     * Jk:
     * HandlerRequest.HOSTBUFFER = 10 CharChunk, buffer for Host decoding
     * WorkerEnv: SSL_CERT_NOTE=16 - MessageBytes containing the cert
     * <p>
     * Catalina CoyoteAdapter:
     * ADAPTER_NOTES = 1 - stores the HttpServletRequest object ( req/res)
     * <p>
     * To avoid conflicts, note in the range 0 - 8 are reserved for the
     * servlet container ( catalina connector, etc ), and values in 9 - 16
     * for connector use.
     * <p>
     * 17-31 range is not allocated or used.
     */
    public final void setNote(int pos, Object value) {
        notes[pos] = value;
    }


    public final Object getNote(int pos) {
        return notes[pos];
    }


    // -------------------- Recycling --------------------


    public void recycle() {
        bytesRead = 0;

        contentLength = -1;
        contentTypeMB = null;
        charEncoding = null;
        headers.recycle();
        serverNameMB.recycle();
        serverPort = -1;
        localPort = -1;
        remotePort = -1;
        available = 0;

        parameters.recycle();

        unparsedURIMB.recycle();
        uriMB.recycle();
        decodedUriMB.recycle();
        queryMB.recycle();
        methodMB.recycle();
        protoMB.recycle();

        schemeMB.recycle();

        instanceId.recycle();
        remoteUser.recycle();
        authType.recycle();
        attributes.clear();
    }

    // -------------------- Info  --------------------
    public void updateCounters() {
        reqProcessorMX.updateCounters();
    }

    public RequestInfo getRequestProcessor() {
        return reqProcessorMX;
    }

    public boolean isProcessing() {
        return reqProcessorMX.getStage() == Constants.STAGE_SERVICE;
    }

}
