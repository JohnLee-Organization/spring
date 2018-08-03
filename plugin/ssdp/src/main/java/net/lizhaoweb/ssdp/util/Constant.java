/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : common
 * @Package : net.lizhaoweb.ssdp.util
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 12:59
 */
package net.lizhaoweb.ssdp.util;

import java.util.UUID;

/**
 * <h1>工具 - 常量</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年11月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class Constant {

    /**
     * SSDP 消息
     */
    public static class Message {
        /**
         * 结束标志
         */
        public static class EOF {
            /**
             * 行结束
             */
            public static final String LINE = "\r\n";
            /**
             * 行结束(正则表达式)
             */
            public static final String REGEX_LINE = "\\r\\n";
        }

        /**
         * 日期格式
         */
        public static class DateFormat {
            public static final String RESPONSE = "EEE, dd MMM yyyy HH:mm:ss z";
        }

        /**
         * 操作系统
         */
        public static class OS {
            public static final UUID SYSTEM_UUID = UUID.randomUUID();
        }
    }
}
