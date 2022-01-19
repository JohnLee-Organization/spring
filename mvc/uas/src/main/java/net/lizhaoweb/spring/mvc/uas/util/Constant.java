/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.mvc.uas.util
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 07:42
 */
package net.lizhaoweb.spring.mvc.uas.util;

/**
 * <h1>工具 - 常量</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年03月04日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface Constant extends net.lizhaoweb.spring.mvc.util.Constant {

    public static interface Application extends net.lizhaoweb.spring.mvc.util.Constant.Application {
        public interface System extends net.lizhaoweb.spring.mvc.util.Constant.Application.System {
            public interface Config extends net.lizhaoweb.spring.mvc.util.Constant.Application.System.Config {
                public static final String USER_SESSION_KEY = "USER_SESSION_KEY";
                public static final String JUMP_URL_KEY = "jumpURL";
                public static final String JUMP_TO_KEY = "jumpTo";
            }
        }

        public static interface Custom {

            public static interface Property {

                public static interface Key {
                    public static final String TAKE_EFFECT = "net.john_lee.uas.take_effect";
                    public static final String CHECK_VALIDATE_CODE = "net.john_lee.uas.login.validate_code.check";
                    public static final String PASSWORD_ENCRYPTION_ALGORITHM = "net.john_lee.uas.login.password.encryption_algorithm";
                }

                public static interface SpringKey {
                    public static final String TAKE_EFFECT = "${net.john_lee.uas.take_effect}";
                    public static final String CHECK_VALIDATE_CODE = "${net.john_lee.uas.login.validate_code.check}";
                    public static final String PASSWORD_ENCRYPTION_ALGORITHM = "${net.john_lee.uas.login.password.encryption_algorithm}";
                }
            }
        }
    }
}
