/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : net.lizhaoweb.coyote
 * @date : 2024-06-07
 * @time : 14:21
 */
package net.lizhaoweb.coyote;

import java.util.Locale;

/**
 * a
 * <p>
 * Created by jhon on 2024/6/7 14:21
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0
 * @email 404644381@qq.com
 */
public final class Constants {


    // -------------------------------------------------------------- Constants


    public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";


    public static final String LOCALE_DEFAULT = "en";


    public static final Locale DEFAULT_LOCALE = new Locale(LOCALE_DEFAULT, "");


    public static final int MAX_NOTES = 32;


    // Request states
    public static final int STAGE_NEW = 0;
    public static final int STAGE_PARSE = 1;
    public static final int STAGE_PREPARE = 2;
    public static final int STAGE_SERVICE = 3;
    public static final int STAGE_ENDINPUT = 4;
    public static final int STAGE_ENDOUTPUT = 5;
    public static final int STAGE_KEEPALIVE = 6;
    public static final int STAGE_ENDED = 7;


    /**
     * Has security been turned on?
     */
    public static final boolean IS_SECURITY_ENABLED = (System.getSecurityManager() != null);


    /**
     * If true, custom HTTP status messages will be used in headers.
     */
    public static final boolean USE_CUSTOM_STATUS_MSG_IN_HEADER = Boolean.valueOf(System.getProperty("org.apache.coyote.USE_CUSTOM_STATUS_MSG_IN_HEADER", "false")).booleanValue();

}
