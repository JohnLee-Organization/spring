/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.servlet
 * @date : 2024-06-04
 * @time : 11:52
 */
package net.lizhaoweb.ssdp.servlet;

import java.io.IOException;

/**
 * Created by jhon on 2024/6/4 11:52
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public interface AsyncDispatcher {

    /**
     * Perform an asynchronous dispatch. The method does not check if the
     * request is in an appropriate state for this; it is the caller's
     * responsibility to check this.
     *
     * @param request  The request object to pass to the dispatch target
     * @param response The response object to pass to the dispatch target
     * @throws SsdpServletException if thrown by the dispatch target
     * @throws IOException          if an I/O error occurs while processing the
     *                              dispatch
     */
    void dispatch(SsdpServletRequest request, SsdpServletResponse response) throws SsdpServletException, IOException;

}
