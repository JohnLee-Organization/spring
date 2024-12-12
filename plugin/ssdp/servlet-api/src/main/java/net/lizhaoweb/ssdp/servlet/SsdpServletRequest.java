/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 10:32
 */
package net.lizhaoweb.ssdp.servlet;

import net.lizhaoweb.ssdp.servlet.http.HttpSsdpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Defines an object to provide client request information to a servlet.  The
 * servlet container creates a <code>ISsdpServletRequest</code> object and passes
 * it as an argument to the servlet's <code>service</code> method.
 *
 * <p>A <code>ISsdpServletRequest</code> object provides data including
 * parameter name and values, attributes, and an input stream.
 * Interfaces that extend <code>ISsdpServletRequest</code> can provide
 * additional protocol-specific data (for example, HTTP data is
 * provided by {@link HttpSsdpServletRequest}.
 * <p>
 * Created by jhon on 2024/6/4 10:32
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see HttpSsdpServletRequest
 */
@SuppressWarnings("unused")
public interface SsdpServletRequest {


    /**
     * Returns the value of the named attribute as an <code>Object</code>,
     * or <code>null</code> if no attribute of the given name exists.
     *
     * <p> Attributes can be set two ways.  The servlet container may set
     * attributes to make available custom information about a request.
     * For example, for requests made using HTTPS, the attribute
     * <code>javax.servlet.request.X509Certificate</code> can be used to
     * retrieve information on the certificate of the client.  Attributes
     * can also be set programatically using
     * {@link #setAttribute}.  This allows information to be
     * embedded into a request before a {@link SsdpRequestDispatcher} call.
     *
     * <p>Attribute names should follow the same conventions as package
     * names. This specification reserves names matching <code>java.*</code>,
     * <code>javax.*</code>, and <code>sun.*</code>.
     *
     * @param name a <code>String</code> specifying the name of
     *             the attribute
     * @param <T> T
     * @return an <code>Object</code> containing the value
     * of the attribute, or <code>null</code> if
     * the attribute does not exist
     */
    <T> T getAttribute(String name);


    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request.
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     *
     * @return an <code>Enumeration</code> of strings
     * containing the names
     * of the request's attributes
     */
    Enumeration<String> getAttributeNames();


    /**
     * Returns the name of the character encoding used in the body of this
     * request. This method returns <code>null</code> if the request
     * does not specify a character encoding
     *
     * @return a <code>String</code> containing the name of
     * the character encoding, or <code>null</code>
     * if the request does not specify a character encoding
     */
    String getCharacterEncoding();


    /**
     * Overrides the name of the character encoding used in the body of this
     * request. This method must be called prior to reading request parameters
     * or reading input using getReader(). Otherwise, it has no effect.
     *
     * @param env <code>String</code> containing the name of
     *            the character encoding.
     * @throws UnsupportedEncodingException if this
     *                                      ISsdpServletRequest is still in a state where a
     *                                      character encoding may be set, but the specified
     *                                      encoding is invalid
     */
    void setCharacterEncoding(String env) throws UnsupportedEncodingException;


    /**
     * Retrieves the body of the request as binary data using
     * a {@link SsdpServletInputStream}.  Either this method or
     * {@link #getReader} may be called to read the body, not both.
     *
     * @return a {@link SsdpServletInputStream} object containing
     * the body of the request
     * @throws IllegalStateException if the {@link #getReader} method
     *                               has already been called for this request
     * @throws IOException           if an input or output exception occurred
     */
    SsdpServletInputStream getInputStream() throws IOException;


    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * <p>You should only use this method when you are sure the
     * parameter has only one value. If the parameter might have
     * more than one value, use {@link #getParameterValues}.
     *
     * <p>If you use this method with a multivalued
     * parameter, the value returned is equal to the first value
     * in the array returned by <code>getParameterValues</code>.
     *
     * <p>If the parameter data was sent in the request body, such as occurs
     * with an HTTP POST request, then reading the body directly via {@link
     * #getInputStream} or {@link #getReader} can interfere
     * with the execution of this method.
     *
     * @param name a <code>String</code> specifying the
     *             name of the parameter
     * @return a <code>String</code> representing the
     * single value of the parameter
     * @see #getParameterValues
     */
    String getParameter(String name);


    /**
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has
     * no parameters, the method returns an
     * empty <code>Enumeration</code>.
     *
     * @return an <code>Enumeration</code> of <code>String</code>
     * objects, each <code>String</code> containing
     * the name of a request parameter; or an
     * empty <code>Enumeration</code> if the
     * request has no parameters
     */
    Enumeration<String> getParameterNames();


    /**
     * Returns an array of <code>String</code> objects containing
     * all of the values the given request parameter has, or
     * <code>null</code> if the parameter does not exist.
     *
     * <p>If the parameter has a single value, the array has a length
     * of 1.
     *
     * @param name a <code>String</code> containing the name of
     *             the parameter whose value is requested
     * @return an array of <code>String</code> objects
     * containing the parameter's values
     * @see #getParameter
     */
    String[] getParameterValues(String name);

    /**
     * Returns a java.util.Map of the parameters of this request.
     * Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * @return an immutable java.util.Map containing parameter names as
     * keys and parameter values as map values. The keys in the parameter
     * map are of type String. The values in the parameter map are of type
     * String array.
     */
    Map<String, String[]> getParameterMap();


    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for
     * example, HTTP/1.1. For HTTP servlets, the value
     * returned is the same as the value of the CGI variable
     * <code>SERVER_PROTOCOL</code>.
     *
     * @return a <code>String</code> containing the protocol
     * name and version number
     */
    String getProtocol();


    /**
     * Returns the name of the scheme used to make this request,
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     *
     * @return a <code>String</code> containing the name
     * of the scheme used to make this request
     */
    String getScheme();


    /**
     * Returns the host name of the server to which the request was sent.
     * It is the value of the part before ":" in the <code>Host</code>
     * header value, if any, or the resolved server name, or the server IP address.
     *
     * @return a <code>String</code> containing the name
     * of the server
     */
    String getServerName();


    /**
     * Returns the port number to which the request was sent.
     * It is the value of the part after ":" in the <code>Host</code>
     * header value, if any, or the server port where the client connection
     * was accepted on.
     *
     * @return an integer specifying the port number
     */
    int getServerPort();


    /**
     * Retrieves the body of the request as character data using
     * a <code>BufferedReader</code>.  The reader translates the character
     * data according to the character encoding used on the body.
     * Either this method or {@link #getInputStream} may be called to read the
     * body, not both.
     *
     * @return a <code>BufferedReader</code>
     * containing the body of the request
     * @throws IllegalStateException if {@link #getInputStream} method
     *                               has been called on this request
     * @throws IOException           if an input or output exception occurred
     * @see #getInputStream
     */
    BufferedReader getReader() throws IOException;


    /**
     * Returns the Internet Protocol (IP) address of the client
     * or last proxy that sent the request.
     * For HTTP servlets, same as the value of the
     * CGI variable <code>REMOTE_ADDR</code>.
     *
     * @return a <code>String</code> containing the
     * IP address of the client that sent the request
     */
    String getRemoteAddr();


    /**
     * Returns the fully qualified name of the client
     * or the last proxy that sent the request.
     * If the engine cannot or chooses not to resolve the hostname
     * (to improve performance), this method returns the dotted-string form of
     * the IP address. For HTTP servlets, same as the value of the CGI variable
     * <code>REMOTE_HOST</code>.
     *
     * @return a <code>String</code> containing the fully
     * qualified name of the client
     */
    String getRemoteHost();


    /**
     * Stores an attribute in this request.
     * Attributes are reset between requests.  This method is most
     * often used in conjunction with {@link SsdpRequestDispatcher}.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     * <br> If the object passed in is null, the effect is the same as
     * calling {@link #removeAttribute}.
     * <br> It is warned that when the request is dispatched from the
     * servlet resides in a different web application by
     * <code>RequestDispatcher</code>, the object set by this method
     * may not be correctly retrieved in the caller servlet.
     *
     * @param name a <code>String</code> specifying
     *             the name of the attribute
     * @param o    the <code>Object</code> to be stored
     */
    void setAttribute(String name, Object o);


    /**
     * Removes an attribute from this request.  This method is not
     * generally needed as attributes only persist as long as the request
     * is being handled.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *
     * @param name a <code>String</code> specifying
     *             the name of the attribute to remove
     */
    void removeAttribute(String name);


    /**
     * Returns the preferred <code>Locale</code> that the client will
     * accept content in, based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header,
     * this method returns the default locale for the server.
     *
     * @return the preferred <code>Locale</code> for the client
     */
    Locale getLocale();


    /**
     * Returns an <code>Enumeration</code> of <code>Locale</code> objects
     * indicating, in decreasing order starting with the preferred locale, the
     * locales that are acceptable to the client based on the Accept-Language
     * header.
     * If the client request doesn't provide an Accept-Language header,
     * this method returns an <code>Enumeration</code> containing one
     * <code>Locale</code>, the default locale for the server.
     *
     * @return an <code>Enumeration</code> of preferred
     * <code>Locale</code> objects for the client
     */
    Enumeration<Locale> getLocales();


    /**
     * Returns a {@link SsdpRequestDispatcher} object that acts as a wrapper for
     * the resource located at the given path.
     * A <code>RequestDispatcher</code> object can be used to forward
     * a request to the resource or to include the resource in a response.
     * The resource can be dynamic or static.
     *
     * <p>The pathname specified may be relative, although it cannot extend
     * outside the current servlet context.  If the path begins with
     * a "/" it is interpreted as relative to the current context root.
     * This method returns <code>null</code> if the servlet container
     * cannot return a <code>RequestDispatcher</code>.
     *
     * <p>The difference between this method and {@link
     * SsdpServletContext#getRequestDispatcher} is that this method can take a
     * relative path.
     *
     * @param path a <code>String</code> specifying the pathname
     *             to the resource. If it is relative, it must be
     *             relative against the current servlet.
     * @return a <code>RequestDispatcher</code> object
     * that acts as a wrapper for the resource
     * at the specified path, or <code>null</code>
     * if the servlet container cannot return a
     * <code>RequestDispatcher</code>
     * @see SsdpRequestDispatcher
     * @see SsdpServletContext#getRequestDispatcher
     */

    SsdpRequestDispatcher getRequestDispatcher(String path);


    /**
     * Returns the Internet Protocol (IP) source port of the client
     * or last proxy that sent the request.
     *
     * @return an integer specifying the port number
     */
    int getRemotePort();


    /**
     * Returns the host name of the Internet Protocol (IP) interface on
     * which the request was received.
     *
     * @return a <code>String</code> containing the host
     * name of the IP on which the request was received.
     */
    String getLocalName();

    /**
     * Returns the Internet Protocol (IP) address of the interface on
     * which the request  was received.
     *
     * @return a <code>String</code> containing the
     * IP address on which the request was received.
     */
    String getLocalAddr();


    /**
     * Returns the Internet Protocol (IP) port number of the interface
     * on which the request was received.
     *
     * @return an integer specifying the port number
     */
    int getLocalPort();

}
