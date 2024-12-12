/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:10
 */
package net.lizhaoweb.coyote;

import lombok.Getter;
import lombok.Setter;

import javax.management.ObjectName;

/**
 * Structure holding the Request and Response objects. It also holds statistical
 * informations about request processing and provide management informations
 * about the requests being processed.
 * <p>
 * Each thread uses a Request/Response pair that is recycled on each request.
 * This object provides a place to collect global low-level statistics - without
 * having to deal with synchronization ( since each thread will have it's own
 * RequestProcessorMX ).
 * <p>
 * TODO: Request notifications will be registered here.
 * <p>
 * Created by jhon on 2024/6/7 14:10
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
@SuppressWarnings("unused")
public class RequestInfo {

    RequestGroupInfo global = null;

    // ----------------------------------------------------------- Constructors

    public RequestInfo(Request req) {
        this.req = req;
    }

    public RequestGroupInfo getGlobalProcessor() {
        return global;
    }

    public void setGlobalProcessor(RequestGroupInfo global) {
        if (global != null) {
            this.global = global;
            global.addRequestProcessor(this);
        } else {
            if (this.global != null) {
                this.global.removeRequestProcessor(this);
                this.global = null;
            }
        }
    }


    // ----------------------------------------------------- Instance Variables
    Request req;
    @Setter
    @Getter
    int stage = Constants.STAGE_NEW;
    @Setter
    @Getter
    String workerThreadName;
    @Setter
    @Getter
    ObjectName rpName;

    // -------------------- Information about the current request  -----------
    // This is useful for long-running requests only

    public String getMethod() {
        return req.method().toString();
    }

    public String getCurrentUri() {
        return req.requestURI().toString();
    }

    public String getCurrentQueryString() {
        return req.queryString().toString();
    }

    public String getProtocol() {
        return req.protocol().toString();
    }

    public String getVirtualHost() {
        return req.serverName().toString();
    }

    public int getServerPort() {
        return req.getServerPort();
    }

    public String getRemoteAddr() {
        req.action(ActionCode.ACTION_REQ_HOST_ADDR_ATTRIBUTE, null);
        return req.remoteAddr().toString();
    }

    public int getContentLength() {
        return req.getContentLength();
    }

    public long getRequestBytesReceived() {
        return req.getBytesRead();
    }

    public long getRequestBytesSent() {
        return req.getResponse().getBytesWritten();
    }

    public long getRequestProcessingTime() {
        if (getStage() == Constants.STAGE_ENDED) return 0;
        else return (System.currentTimeMillis() - req.getStartTime());
    }

    // -------------------- Statistical data  --------------------
    // Collected at the end of each request.
    @Setter
    @Getter
    private long bytesSent;
    @Setter
    @Getter
    private long bytesReceived;

    // Total time = divide by requestCount to get average.
    @Setter
    @Getter
    private long processingTime;
    // The longest response time for a request
    @Setter
    @Getter
    private long maxTime;
    // URI of the request that took maxTime
    @Setter
    @Getter
    private String maxRequestUri;

    @Setter
    @Getter
    private int requestCount;
    // number of response codes >= 400
    @Setter
    @Getter
    private int errorCount;

    //the time of the last request
    @Setter
    @Getter
    private long lastRequestProcessingTime = 0;


    /**
     * Called by the processor before recycling the request. It'll collect
     * statistic information.
     */
    void updateCounters() {
        bytesReceived += req.getBytesRead();
        bytesSent += req.getResponse().getBytesWritten();
        requestCount++;
        if (req.getResponse().getStatus() >= 400) errorCount++;
        long t0 = req.getStartTime();
        long t1 = System.currentTimeMillis();
        long time = t1 - t0;
        this.lastRequestProcessingTime = time;
        processingTime += time;
        if (maxTime < time) {
            maxTime = time;
            maxRequestUri = req.requestURI().toString();
        }
    }

}
