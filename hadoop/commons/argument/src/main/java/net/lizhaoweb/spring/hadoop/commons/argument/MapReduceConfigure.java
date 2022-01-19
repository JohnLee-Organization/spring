/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.hadoop.commons.argument
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 19:25
 */
package net.lizhaoweb.spring.hadoop.commons.argument;

import org.apache.commons.cli.*;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2017年07月27日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class MapReduceConfigure {

    private static Options options = new Options();

    public static void addOption(Option... optionArray) {
        for (Option option : optionArray) {
            options.addOption(option);
        }
    }

    public static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        return parser.parse(options, args);
    }
}
