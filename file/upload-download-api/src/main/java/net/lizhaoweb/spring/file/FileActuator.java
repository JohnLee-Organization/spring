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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class FileActuator implements IActuator<FileContext> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Setter
    private List<IHandler<FileContext>> beforeFileExecuteHandlers;

    @Setter
    private List<IHandler<FileContext>> afterFileExecuteHandlers;

    @Setter
    private IExecutor<FileContext> executor;

    @Override
    public void actuate(FileContext context) {
        if (context == null) {
            throw new IllegalArgumentException("The context for actuate file is null.");
        }

        // 执行单个文件处理前
        if (this.executeHandlers(context, this.beforeFileExecuteHandlers)) return;

        // 执行单个文件处理
        if (!this.executor.execute(context)) return;


        // 执行单个文件处理后
        if (this.executeHandlers(context, this.afterFileExecuteHandlers)) return;

        // 最后处理
        logger.error(FileConstant.logger.marker.FATAL, "The file-actuator is actuate-done.");
    }

    public void actuate(FileConfig config) {
        FileContext context = new FileContext(config);
        this.actuate(context);
    }

    private boolean executeHandlers(FileContext context, List<IHandler<FileContext>> handlers) {
        if (handlers == null) {
            return false;
        }
        for (IHandler<FileContext> handler : handlers) {
            try {
                if (!handler.handle(context)) {
                    return true;
                }
            } catch (Throwable e) {
                if (!context.continueOnExceptionForLoopFileHandler()) {
                    throw e;
                } else {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }
}
