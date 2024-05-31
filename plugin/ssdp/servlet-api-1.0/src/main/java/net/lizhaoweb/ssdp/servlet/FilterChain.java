/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-05-30
 * @time : 15:09
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.IOException;

/**
 * A FilterChain is an object provided by the servlet container to the developer
 * giving a view into the invocation chain of a filtered request for a resource. Filters
 * use the FilterChain to invoke the next filter in the chain, or if the calling filter
 * is the last filter in the chain, to invoke the resource at the end of the chain.
 * <p>
 * Created by jhon on 2024/5/30 15:09
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 * @see Filter
 */
@SuppressWarnings("unused")
public interface FilterChain {

    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param request  the request to pass along the chain.
     * @param response the response to pass along the chain.
     */
    void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException;

}
