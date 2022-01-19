/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.hive.plugin.path
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:33
 */
package net.lizhaoweb.hive.plugin.path;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title FileFilterExcludeTmpFiles.java
 * Description hive加载分区表时会加载.tmp的文件，该类型文件在flume滚动数据之后就会消失，此时hive找不到该文件就会报错 该类会将.tmp的文件过滤掉，不加载进hive的分区表中
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月24日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileFilterExcludeTmpFiles implements PathFilter {

    private static final Logger logger = LoggerFactory.getLogger(FileFilterExcludeTmpFiles.class);

    public boolean accept(Path path) {
        String name = path.getName();
        logger.debug("The path name is {}", name);
        return !name.startsWith("_") && !name.startsWith(".") && !name.endsWith(".tmp");
    }
}
