/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:58
 */
package net.lizhaoweb.spring.netty.server.config;

import lombok.Data;

/**
 * Netty 配置 - 对象
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2018年12月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Data
public class ObjectConfiguration extends BasicConfiguration {

    // 设置对象序列化最大长度为1M 防止内存溢出
    private int maxObjectSizeForDecoder = 1024 * 1024;

    public ObjectConfiguration(String host, int port) {
        super(host, port);
    }
}
