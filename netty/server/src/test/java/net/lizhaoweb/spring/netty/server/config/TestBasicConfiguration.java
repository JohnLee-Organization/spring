/**
 * Copyright (c) 2020, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.netty.server.config
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:37
 */
package net.lizhaoweb.spring.netty.server.config;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2020年07月20日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class TestBasicConfiguration {

    public static void main(String[] args) {
        BasicConfiguration configuration = new BasicConfiguration("host", 123);
        configuration.setConfig("byte", (byte) 1);
        configuration.setConfig("Byte", new Byte((byte) 1));
        configuration.setConfig("Short", new Short((short) 1));
        configuration.setConfig("short", (short) 1);
        configuration.setConfig("Integer", (Integer) 1);
        configuration.setConfig("int", (int) 1);
        configuration.setConfig("Long", new Long(1));
        configuration.setConfig("long", (long) 1);
        configuration.setConfig("Float", new Float(1));
        configuration.setConfig("float", (float) 1);
        configuration.setConfig("Double", new Double(1));
        configuration.setConfig("double", (double) 1);
        configuration.setConfig("Character", new Character('a'));
        configuration.setConfig("char", 'a');
        configuration.setConfig("Boolean", true);
        configuration.setConfig("boolean", true);
        configuration.setConfig("String", "1");
        configuration.setConfig("Date", new Date());
        configuration.setConfig("Pattern", Pattern.compile(""));


        System.out.println(configuration.getConfig("byte", (byte) 0));
        System.out.println(configuration.getConfig("Byte", new Byte((byte) 0)));
        System.out.println(configuration.getConfig("Short", new Short((short) 0)));
        System.out.println(configuration.getConfig("short", (short) 0));
        System.out.println(configuration.getConfig("Integer", (Integer) 0));
        System.out.println(configuration.getConfig("int", (int) 0));
        System.out.println(configuration.getConfig("Long", new Long(0)));
        System.out.println(configuration.getConfig("long", (long) 0));
        System.out.println(configuration.getConfig("Float", new Float(0)));
        System.out.println(configuration.getConfig("float", (float) 0));
        System.out.println(configuration.getConfig("Double", new Double(0)));
        System.out.println(configuration.getConfig("double", (double) 0));
        System.out.println(configuration.getConfig("Character", new Character('a')));
        System.out.println(configuration.getConfig("char", 'a'));
        System.out.println(configuration.getConfig("Boolean", true));
        System.out.println(configuration.getConfig("boolean", true));
        System.out.println(configuration.getConfig("String", "0"));
        System.out.println(configuration.getConfig("Date", new Date()));
        System.out.println(configuration.getConfig("Pattern", Pattern.compile("")));
    }
}
