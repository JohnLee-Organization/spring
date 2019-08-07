/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:07
 */
package net.lizhaoweb.spring.file;

import lombok.Setter;

import java.util.List;

/**
 * <h1>启动器 [实现] —— 文件处理</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2019年08月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileActuator implements IFileActuator {

    @Setter
    private FileConfig config;

    @Setter
    private List<IBeforeFileExecuteHandler> beforeFileExecuteHandlers;

    @Setter
    private List<IAfterFileExecuteHandler> afterFileExecuteHandlers;

    @Setter
    private IFileExecutor executor;

    @Override
    public void actuate(FileConfig config) {
        FileContext context = new FileContext(config);

        // 执行文件处理前
        if (beforeFileExecuteHandlers != null) {
            for (IBeforeFileExecuteHandler handler : beforeFileExecuteHandlers) {
                if (!handler.handle(context)) {
                    return;
                }
            }
        }

        // 执行文件处理
        if (!executor.execute(context)) {
            return;
        }

        // 执行文件处理后
        if (afterFileExecuteHandlers != null) {
            for (IAfterFileExecuteHandler handler : afterFileExecuteHandlers) {
                if (!handler.handle(context)) {
                    return;
                }
            }
        }
    }
}
