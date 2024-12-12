/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin.comet
 * @date : 2024-06-11
 * @time : 09:03
 */
package net.lizhaoweb.quentin.comet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The CometEvent interface.
 * <p>
 * Created by jhon on 2024/6/11 9:03
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface CometEvent {

    /**
     * Enumeration describing the major events that the container can invoke
     * the CometProcessors event() method with
     * BEGIN - will be called at the beginning
     * of the processing of the connection. It can be used to initialize any relevant
     * fields using the request and response objects. Between the end of the processing
     * of this event, and the beginning of the processing of the end or error events,
     * it is possible to use the response object to write data on the open connection.
     * Note that the response object and dependent OutputStream and Writer are still
     * not synchronized, so when they are accessed by multiple threads,
     * synchronization is mandatory. After processing the initial event, the request
     * is considered to be committed.
     * READ - This indicates that input data is available, and that one read can be made
     * without blocking. The available and ready methods of the InputStream or
     * Reader may be used to determine if there is a risk of blocking: the servlet
     * should read while data is reported available. When encountering a read error,
     * the servlet should report it by propagating the exception properly. Throwing
     * an exception will cause the error event to be invoked, and the connection
     * will be closed.
     * Alternately, it is also possible to catch any exception, perform clean up
     * on any data structure the servlet may be using, and using the close method
     * of the event. It is not allowed to attempt reading data from the request
     * object outside of the execution of this method.
     * END - End may be called to end the processing of the request. Fields that have
     * been initialized in the begin method should be reset. After this event has
     * been processed, the request and response objects, as well as all their dependent
     * objects will be recycled and used to process other requests. End will also be
     * called when data is available and the end of file is reached on the request input
     * (this usually indicates the client has pipelined a request).
     * ERROR - Error will be called by the container in the case where an IO exception
     * or a similar unrecoverable error occurs on the connection. Fields that have
     * been initialized in the begin method should be reset. After this event has
     * been processed, the request and response objects, as well as all their dependent
     * objects will be recycled and used to process other requests.
     */
    enum EventType {BEGIN, READ, END, ERROR}


    /**
     * Event details
     * TIMEOUT - the connection timed out (sub type of ERROR); note that this ERROR type is not fatal, and
     * the connection will not be closed unless the servlet uses the close method of the event
     * CLIENT_DISCONNECT - the client connection was closed (sub type of ERROR)
     * IOEXCEPTION - an IO exception occurred, such as invalid content, for example, an invalid chunk block (sub type of ERROR)
     * WEBAPP_RELOAD - the webapplication is being reloaded (sub type of END)
     * SERVER_SHUTDOWN - the server is shutting down (sub type of END)
     * SESSION_END - the servlet ended the session (sub type of END)
     */
    enum EventSubType {TIMEOUT, CLIENT_DISCONNECT, IOEXCEPTION, WEBAPP_RELOAD, SERVER_SHUTDOWN, SESSION_END}


    /**
     * Returns the HttpServletRequest.
     *
     * @return HttpServletRequest
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * Returns the HttpServletResponse.
     *
     * @return HttpServletResponse
     */
    HttpServletResponse getHttpServletResponse();

    /**
     * Returns the event type.
     *
     * @return EventType
     */
    EventType getEventType();

    /**
     * Returns the sub type of this event.
     *
     * @return EventSubType
     */
    EventSubType getEventSubType();

    /**
     * Ends the Comet session. This signals to the container that
     * the container wants to end the comet session. This will send back to the
     * client a notice that the server has no more data to send as part of this
     * request. The servlet should perform any needed cleanup as if it had received
     * an END or ERROR event.
     *
     * @throws IOException if an IO exception occurs
     */
    void close() throws IOException;

    /**
     * Sets the timeout for this Comet connection. Please NOTE, that the implementation
     * of a per connection timeout is OPTIONAL and MAY NOT be implemented.<br/>
     * This method sets the timeout in milliseconds of idle time on the connection.
     * The timeout is reset every time data is received from the connection or data is flushed
     * using <code>response.flushBuffer()</code>. If a timeout occurs, the
     * <code>error(HttpServletRequest, HttpServletResponse)</code> method is invoked. The
     * web application SHOULD NOT attempt to reuse the request and response objects after a timeout
     * as the <code>error(HttpServletRequest, HttpServletResponse)</code> method indicates.<br/>
     * This method should not be called asynchronously, as that will have no effect.
     *
     * @param timeout The timeout in milliseconds for this connection, must be a positive value, larger than 0
     * @throws IOException                   An IOException may be thrown to indicate an IO error,
     *                                       or that the EOF has been reached on the connection
     * @throws ServletException              An exception has occurred, as specified by the root
     *                                       cause
     * @throws UnsupportedOperationException if per connection timeout is not supported, either at all or at this phase
     *                                       of the invocation.
     */
    void setTimeout(int timeout) throws IOException, ServletException, UnsupportedOperationException;

}
