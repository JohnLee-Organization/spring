/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.ssdp.util
 * @date : 2024-03-06
 * @time : 13:32
 */
package net.lizhaoweb.ssdp.util;

import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * [工具] 常量
 * <p>
 * Created by Jhon.Lee on 3/6/2024 13:32
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 */
@SuppressWarnings({"unused"})
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
         * 时区
         */
        public static class _TimeZone {
            public static final TimeZone RESPONSE = TimeZone.getTimeZone("0");
        }

        /**
         * 方言
         */
        public static class _Locale {
            public static final Locale RESPONSE = new Locale("ch.en");
        }

        /**
         * 操作系统
         */
        public static class OS {
            public static final UUID SYSTEM_UUID = UUID.randomUUID();
        }
    }
}
