/*
 * Copyright (c) 2024, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 * @project : spring
 * @package : de.schlichtherle.util
 * @date : 2024-04-01
 * @time : 10:53
 */
package de.schlichtherle.util;

import org.junit.BeforeClass;
import org.junit.Test;

import static de.schlichtherle.util.ObfuscatedString.obfuscate;

/**
 * a
 * <p>
 * Created by Jhon.Lee on 2024/4/1 10:53
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0
 * @email 404644381@qq.com
 */
public class TestObfuscatedString {

    @BeforeClass
    public static void setUpBeforeClass() {
        // 设置系统属性，指定logback配置文件位置
        System.setProperty("logback.configurationFile", "classpath:logback.xml");
    }

    @Test
    public void LicenseInterceptor() {
        String[] args = new String[]{
                "code",
                "0319",
                "msg",
                "0319"
        };
        for (String arg : args) {
            System.out.println(obfuscate(arg));
        }
    }
}
