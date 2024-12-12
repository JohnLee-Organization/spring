/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote.http11
 * @date : 2024-06-07
 * @time : 10:37
 */
package net.lizhaoweb.coyote.http11;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lizhaoweb.coyote.Adapter;
import net.lizhaoweb.coyote.Request;
import net.lizhaoweb.coyote.Response;
import net.lizhaoweb.mikefox.buf.Ascii;
import net.lizhaoweb.mikefox.buf.ByteChunk;
import net.lizhaoweb.mikefox.buf.MessageBytes;
import net.lizhaoweb.mikefox.res.StringManager;

import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by jhon on 2024/6/7 10:37
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@Slf4j
@SuppressWarnings("unused")
public class AbstractHttp11Processor {

    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);

    /**
     * Associated adapter.
     */
    @Setter
    @Getter
    protected Adapter adapter = null;


    /**
     * Request object.
     */
    @Getter
    protected Request request = null;


    /**
     * Response object.
     */
    protected Response response = null;


    /**
     * Error flag.
     */
    protected boolean error = false;


    /**
     * Keep-alive.
     */
    protected boolean keepAlive = true;


    /**
     * HTTP/1.1 flag.
     */
    protected boolean http11 = true;


    /**
     * HTTP/0.9 flag.
     */
    protected boolean http09 = false;


    /**
     * Content delimiter for the request (if false, the connection will
     * be closed at the end of the request).
     */
    protected boolean contentDelimitation = true;


    /**
     * Is there an expectation ?
     */
    protected boolean expectation = false;


    /**
     * List of restricted user agents.
     */
    @Setter
    protected Pattern[] restrictedUserAgents = null;


    /**
     * Maximum number of Keep-Alive requests to honor.
     */
    @Setter
    @Getter
    protected int maxKeepAliveRequests = -1;

    /**
     * The number of seconds Tomcat will wait for a subsequent request
     * before closing the connection.
     */
    @Setter
    @Getter
    protected int timeToLive = -1;

    /**
     * Remote Address associated with the current connection.
     */
    protected String remoteAddr = null;


    /**
     * Remote Host associated with the current connection.
     */
    protected String remoteHost = null;


    /**
     * Local Host associated with the current connection.
     */
    protected String localName = null;


    /**
     * Local port to which the socket is connected
     */
    protected int localPort = -1;


    /**
     * Remote port to which the socket is connected
     */
    protected int remotePort = -1;


    /**
     * The local Host address.
     */
    protected String localAddr = null;


    /**
     * Maximum timeout on uploads. 5 minutes as in Apache HTTPD server.
     */
    @Setter
    @Getter
    protected int timeout = 300000;


    /**
     * Flag to disable setting a different time-out on uploads.
     */
    @Setter
    @Getter
    protected boolean disableUploadTimeout = false;


    /**
     * Allowed compression level.
     */
    protected int compressionLevel = 0;


    /**
     * Minimum content size to make compression.
     */
    @Setter
    protected int compressionMinSize = 2048;


    /**
     * Socket buffering.
     */
    @Setter
    @Getter
    protected int socketBuffer = -1;


    /**
     * Max saved post size.
     */
    @Setter
    @Getter
    protected int maxSavePostSize = 4 * 1024;


    /**
     * List of user agents to not use gzip with
     */
    @Setter
    protected Pattern noCompressionUserAgents[] = null;

    /**
     * List of MIMES which could be gzipped
     */
    @Setter
    protected String[] compressableMimeTypes = {"text/html", "text/xml", "text/plain"};


    /**
     * Host name (used to avoid useless B2C conversion on the host name).
     */
    protected char[] hostNameC = new char[0];


    /**
     * Allow a customized the server header for the tin-foil hat folks.
     */
    @Getter
    protected String server = null;

    /**
     * Set compression level.
     */
    public void setCompression(String compression) {
        switch (compression) {
            case "on":
                this.compressionLevel = 1;
                break;
            case "force":
                this.compressionLevel = 2;
                break;
            case "off":
                this.compressionLevel = 0;
                break;
            default:
                try {
                    // Try to parse compression as an int, which would give the
                    // minimum compression size
                    compressionMinSize = Integer.parseInt(compression);
                    this.compressionLevel = 1;
                } catch (Exception e) {
                    this.compressionLevel = 0;
                }
                break;
        }
    }


    /**
     * Add user-agent for which gzip compression didn't works
     * The user agent String given will be exactly matched
     * to the user-agent header submitted by the client.
     *
     * @param userAgent user-agent string
     */
    public void addNoCompressionUserAgent(String userAgent) {
        try {
            Pattern nRule = Pattern.compile(userAgent);
            noCompressionUserAgents = addREArray(noCompressionUserAgents, nRule);
        } catch (PatternSyntaxException pse) {
            log.error(sm.getString("http11processor.regexp.error", userAgent), pse);
        }
    }


//    /**
//     * Set no compression user agent list (this method is best when used with
//     * a large number of connectors, where it would be better to have all of
//     * them referenced a single array).
//     */
//    public void setNoCompressionUserAgents(Pattern[] noCompressionUserAgents) {
//        this.noCompressionUserAgents = noCompressionUserAgents;
//    }


    /**
     * Set no compression user agent list.
     * List contains users agents separated by ',' :
     * <p>
     * ie: "gorilla,desesplorer,tigrus"
     */
    public void setNoCompressionUserAgents(String noCompressionUserAgents) {
        if (noCompressionUserAgents != null) {
            StringTokenizer st = new StringTokenizer(noCompressionUserAgents, ",");

            while (st.hasMoreTokens()) {
                addNoCompressionUserAgent(st.nextToken().trim());
            }
        }
    }

    /**
     * Add a mime-type which will be compressible
     * The mime-type String will be exactly matched
     * in the response mime-type header .
     *
     * @param mimeType mime-type string
     */
    public void addCompressableMimeType(String mimeType) {
        compressableMimeTypes = addStringArray(compressableMimeTypes, mimeType);
    }


//    /**
//     * Set compressible mime-type list (this method is best when used with
//     * a large number of connectors, where it would be better to have all of
//     * them referenced a single array).
//     */
//    public void setCompressableMimeTypes(String[] compressableMimeTypes) {
//        this.compressableMimeTypes = compressableMimeTypes;
//    }


    /**
     * Set compressable mime-type list
     * List contains users agents separated by ',' :
     * <p>
     * ie: "text/html,text/xml,text/plain"
     */
    public void setCompressableMimeTypes(String compressableMimeTypes) {
        if (compressableMimeTypes != null) {
            this.compressableMimeTypes = null;
            StringTokenizer st = new StringTokenizer(compressableMimeTypes, ",");

            while (st.hasMoreTokens()) {
                addCompressableMimeType(st.nextToken().trim());
            }
        }
    }


    /**
     * Return the list of compressable mime-types.
     */
    public String[] findCompressableMimeTypes() {
        return (compressableMimeTypes);
    }


    /**
     * Return compression level.
     */
    public String getCompression() {
        switch (compressionLevel) {
            case 0:
                return "off";
            case 1:
                return "on";
            case 2:
                return "force";
        }
        return "off";
    }


    /**
     * General use method
     *
     * @param sArray the StringArray
     * @param value  string
     */
    private String[] addStringArray(String[] sArray, String value) {
        String[] result = null;
        if (sArray == null) {
            result = new String[1];
            result[0] = value;
        } else {
            result = new String[sArray.length + 1];
            System.arraycopy(sArray, 0, result, 0, sArray.length);
            result[sArray.length] = value;
        }
        return result;
    }


    /**
     * General use method
     *
     * @param rArray the REArray
     * @param value  Obj
     */
    private Pattern[] addREArray(Pattern[] rArray, Pattern value) {
        Pattern[] result = null;
        if (rArray == null) {
            result = new Pattern[1];
            result[0] = value;
        } else {
            result = new Pattern[rArray.length + 1];
            System.arraycopy(rArray, 0, result, 0, rArray.length);
            result[rArray.length] = value;
        }
        return result;
    }


    /**
     * Checks if any entry in the string array starts with the specified value
     *
     * @param sArray the StringArray
     * @param value  string
     */
    private boolean startsWithStringArray(String[] sArray, String value) {
        if (value == null) return false;
        for (String s : sArray) {
            if (value.startsWith(s)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Add restricted user-agent (which will downgrade the connector
     * to HTTP/1.0 mode). The user agent String given will be matched
     * via regexp to the user-agent header submitted by the client.
     *
     * @param userAgent user-agent string
     */
    public void addRestrictedUserAgent(String userAgent) {
        try {
            Pattern nRule = Pattern.compile(userAgent);
            restrictedUserAgents = addREArray(restrictedUserAgents, nRule);
        } catch (PatternSyntaxException pse) {
            log.error(sm.getString("http11processor.regexp.error", userAgent), pse);
        }
    }


//    /**
//     * Set restricted user agent list (this method is best when used with
//     * a large number of connectors, where it would be better to have all of
//     * them referenced a single array).
//     */
//    public void setRestrictedUserAgents(Pattern[] restrictedUserAgents) {
//        this.restrictedUserAgents = restrictedUserAgents;
//    }


    /**
     * Set restricted user agent list (which will downgrade the connector
     * to HTTP/1.0 mode). List contains users agents separated by ',' :
     * <p>
     * ie: "gorilla,desesplorer,tigrus"
     */
    public void setRestrictedUserAgents(String restrictedUserAgents) {
        if (restrictedUserAgents != null) {
            StringTokenizer st = new StringTokenizer(restrictedUserAgents, ",");
            while (st.hasMoreTokens()) {
                addRestrictedUserAgent(st.nextToken().trim());
            }
        }
    }


    /**
     * Return the list of restricted user agents.
     */
    public String[] findRestrictedUserAgents() {
        String[] sarr = new String[restrictedUserAgents.length];
        for (int i = 0; i < restrictedUserAgents.length; i++)
            sarr[i] = restrictedUserAgents[i].toString();
        return (sarr);
    }


    /**
     * Set the server header name.
     */
    public void setServer(String server) {
        if (server == null || server.equals("")) {
            this.server = null;
        } else {
            this.server = server;
        }
    }


    /**
     * Check for compression
     */
    protected boolean isCompressable() {

        // Nope Compression could works in HTTP 1.0 also
        // cf: mod_deflate

        // Compression only since HTTP 1.1
        // if (! http11)
        //    return false;

        // Check if browser support gzip encoding
        MessageBytes acceptEncodingMB = request.getMimeHeaders().getValue("accept-encoding");

        if ((acceptEncodingMB == null) || (acceptEncodingMB.indexOf("gzip") == -1)) return false;

        // Check if content is not already gzipped
        MessageBytes contentEncodingMB = response.getMimeHeaders().getValue("Content-Encoding");

        if ((contentEncodingMB != null) && (contentEncodingMB.indexOf("gzip") != -1)) return false;

        // If force mode, always compress (test purposes only)
        if (compressionLevel == 2) return true;

        // Check for incompatible Browser
        if (noCompressionUserAgents != null) {
            MessageBytes userAgentValueMB = request.getMimeHeaders().getValue("user-agent");
            if (userAgentValueMB != null) {
                String userAgentValue = userAgentValueMB.toString();

                // If one Regexp rule match, disable compression
                for (Pattern noCompressionUserAgent : noCompressionUserAgents) {
                    assert userAgentValue != null;
                    if (noCompressionUserAgent.matcher(userAgentValue).matches()) return false;
                }
            }
        }

        // Check if sufficient length to trigger the compression
        long contentLength = response.getContentLengthLong();
        if ((contentLength == -1) || (contentLength > compressionMinSize)) {
            // Check for compatible MIME-TYPE
            if (compressableMimeTypes != null) {
                return (startsWithStringArray(compressableMimeTypes, response.getContentType()));
            }
        }

        return false;
    }


    /**
     * Specialized utility method: find a sequence of lower case bytes inside
     * a ByteChunk.
     */
    protected int findBytes(ByteChunk bc, byte[] b) {
        byte first = b[0];
        byte[] buff = bc.getBuffer();
        int start = bc.getStart();
        int end = bc.getEnd();

        // Look for first char
        int srcEnd = b.length;

        for (int i = start; i <= (end - srcEnd); i++) {
            if (Ascii.toLower(buff[i]) != first) continue;
            // found first char, now look for a match
            int myPos = i + 1;
            for (int srcPos = 1; srcPos < srcEnd; ) {
                if (Ascii.toLower(buff[myPos++]) != b[srcPos++]) break;
                if (srcPos == srcEnd) return i - start; // found it
            }
        }
        return -1;
    }


    /**
     * Determine if we must drop the connection because of the HTTP status
     * code.  Use the same list of codes as Apache/httpd.
     */
    protected boolean statusDropsConnection(int status) {
        return status == 400 //* SC_BAD_REQUEST */
                || status == 408 //* SC_REQUEST_TIMEOUT */
                || status == 411 //* SC_LENGTH_REQUIRED */
                || status == 413 //* SC_REQUEST_ENTITY_TOO_LARGE */
                || status == 414 //* SC_REQUEST_URI_TOO_LONG */
                || status == 500 //* SC_INTERNAL_SERVER_ERROR */
                || status == 503 //* SC_SERVICE_UNAVAILABLE */
                || status == 501 //* SC_NOT_IMPLEMENTED */
                ;
    }

}
