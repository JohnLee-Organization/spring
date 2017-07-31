/**
 * Copyright (c) 2017, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.plugin.mybatis.executor
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 09:36
 */
package net.lizhaoweb.spring.plugin.mybatis.executor;

import java.util.List;

/**
 * <h1>执行器 [接口] - SQL</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2017年02月12日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface SQLExecutor {

    /**
     * 执行
     */
    void execute();

    /**
     * 设置 SQL 脚本
     *
     * @param sqlScript SQL 脚本
     */
    void setSQLScript(String sqlScript);

    /**
     * 设置 SQL 脚本列表
     *
     * @param sqlScriptList SQL 脚本列表
     */
    void setSQLScriptList(List<String> sqlScriptList);
}
