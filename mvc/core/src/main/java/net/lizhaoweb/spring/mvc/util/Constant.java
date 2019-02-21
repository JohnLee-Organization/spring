/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : LiZhao Spring MVC Core
 * @Title : Constant.java
 * @Package : net.lizhaoweb.spring.mvc.util
 * @author <a href="http://www.lizhaoweb.net">李召(Jhon.Lee)</a>
 * @Date : 2016年4月28日
 * @Time : 下午1:41:52
 */
package net.lizhaoweb.spring.mvc.util;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * <h1>工具 - 常量</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年4月28日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface Constant extends net.lizhaoweb.common.util.base.Constant {

    public static interface logger {
        public static interface marker {
            public static Marker TRACE = MarkerFactory.getMarker("TRACE");
            public static Marker DEBUG = MarkerFactory.getMarker("DEBUG");
            public static Marker INFO = MarkerFactory.getMarker("INFO");
            public static Marker WARN = MarkerFactory.getMarker("WARN");
            public static Marker ERROR = MarkerFactory.getMarker("ERROR");
            public static Marker FATAL = MarkerFactory.getMarker("FATAL");
        }
    }

    /**
     * MVC核心
     *
     * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
     * @version Constant
     * @notes Created on 2016年5月20日<br>
     * Revision of last commit:$Revision$<br>
     * Author of last commit:$Author$<br>
     * Date of last commit:$Date$<br>
     */
    public static interface MVCCore extends Product {
        /**
         * 应用中路径参数字典键
         */
        public static final String APPLICATION_PATH_VARIABLE_MAP = MVCCore.class.getName() + ".APPLICATION_PATH_VARIABLE_MAP";
    }

    /**
     * 应用
     *
     * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
     * @version Constant
     * @notes Created on 2016年5月23日<br>
     * Revision of last commit:$Revision$<br>
     * Author of last commit:$Author$<br>
     * Date of last commit:$Date$<br>
     */
    public static interface Application {
        /**
         * 应用上下文
         *
         * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
         * @version Constant.Application
         * @notes Created on 2016年5月23日<br>
         * Revision of last commit:$Revision$<br>
         * Author of last commit:$Author$<br>
         * Date of last commit:$Date$<br>
         */
        public static interface ServletContext {
            /**
             * 键
             *
             * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
             * @version Constant.Application.ServletContext
             * @notes Created on 2016年5月23日<br>
             * Revision of last commit:$Revision$<br>
             * Author of last commit:$Author$<br>
             * Date of last commit:$Date$<br>
             */
            public static interface Key {
                /**
                 * 分页大小列表键
                 */
                public static final String PAGE_SIZE_LIST = Key.class.getName() + ".PAGE_SIZE_LIST";
            }
        }

        /**
         * 系统
         *
         * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
         * @version Constant.Application
         * @notes Created on 2016年5月23日<br>
         * Revision of last commit:$Revision$<br>
         * Author of last commit:$Author$<br>
         * Date of last commit:$Date$<br>
         */
        public static interface System {
            /**
             * 分页
             *
             * @author <a href="http://www.lizhaoweb.cn">李召(Jhon.Lee)</a>
             * @version Constant.Application.System
             * @notes Created on 2016年5月23日<br>
             * Revision of last commit:$Revision$<br>
             * Author of last commit:$Author$<br>
             * Date of last commit:$Date$<br>
             */
            public static interface Page {
                /**
                 * 分頁大小列表配置键
                 */
                public static final String SIZE_CONFIG_KEY = Page.class.getName() + ".SIZE_CONFIG_KEY";
            }

            public static interface Config {
                public static final String USER_SESSION_KEY = Config.class.getName() + ".USER_SESSION_KEY";
            }
        }
    }
}
