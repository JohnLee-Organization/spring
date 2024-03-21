/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service
 * @date : 2024-03-06
 * @time : 13:44
 */
package net.lizhaoweb.ssdp.service;

/**
 * [业务层] [接口] 接收器
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:44
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
public interface ISsdpReceiver<REQ, RES> {

    /**
     * 接收。
     *
     * @return 消息包
     */
    RES receive(REQ req);
}
