/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.service
 * @date : 2024-05-15
 * @time : 11:27
 */
package net.lizhaoweb.ssdp.service;

import net.lizhaoweb.ssdp.config.SsdpConfig;

/**
 * [接口] SSDP配置服务
 * <p>
 * Created by jhon on 2024/5/15 11:27
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public interface ISsdpConfigurationService {

    SsdpConfig loadConfig(String... configFiles);
}
