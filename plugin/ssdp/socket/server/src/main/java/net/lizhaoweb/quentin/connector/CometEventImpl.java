/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.quentin.connector
 * @date : 2024-06-11
 * @time : 08:59
 */
package net.lizhaoweb.quentin.connector;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.mikefox.res.StringManager;
import net.lizhaoweb.quentin.comet.CometEvent;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * a
 * <p>
 * Created by jhon on 2024/6/11 8:59
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public class CometEventImpl implements CometEvent {


    /**
     * The string manager for this package.
     */
    protected static final StringManager sm = StringManager.getManager(Constants.Package);


    public CometEventImpl(Request request, Response response) {
        this.request = request;
        this.response = response;
    }


    // ----------------------------------------------------- Instance Variables


    /**
     * Associated request.
     */
    protected Request request = null;


    /**
     * Associated response.
     */
    protected Response response = null;


    /**
     * Event type.
     */
    @Setter
    @Getter
    protected EventType eventType = EventType.BEGIN;


    /**
     * Event sub type.
     */
    @Setter
    @Getter
    protected EventSubType eventSubType = null;


    // --------------------------------------------------------- Public Methods

    /**
     * Clear the event.
     */
    public void clear() {
        request = null;
        response = null;
    }

    public void close() throws IOException {
        if (request == null) {
            throw new IllegalStateException(sm.getString("cometEvent.nullRequest"));
        }
        boolean iscomet = request.isComet();
        request.setComet(false);
        response.finishResponse();
        if (iscomet) request.cometClose();
    }

    public HttpServletRequest getHttpServletRequest() {
        return request.getRequest();
    }

    public HttpServletResponse getHttpServletResponse() {
        return response.getResponse();
    }

    public void setTimeout(int timeout) throws IOException, ServletException, UnsupportedOperationException {
        if (request.getAttribute("net.lizhaoweb.mikefox.comet.timeout.support") == Boolean.TRUE) {
            request.setAttribute("net.lizhaoweb.mikefox.comet.timeout", timeout);
            if (request.isComet()) request.setCometTimeout(timeout);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(super.toString());
        buf.append("[EventType:");
        buf.append(eventType);
        buf.append(", EventSubType:");
        buf.append(eventSubType);
        buf.append("]");
        return buf.toString();
    }

}
