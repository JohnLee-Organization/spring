/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
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
 * @email 404644381@qq.com
 * @notes Created on 2019年08月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileEngine implements IEngine {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Setter
    private EngineConfig config;

    @Setter
    private List<IHandler<EngineContext>> beforeFilesExecuteHandlers;

    @Setter
    private List<IHandler<EngineContext>> afterFilesExecuteHandlers;

    @Setter
    private IActuator<FileContext> actuator;

    private EngineContext context;

    @Override
    public void init() {
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine is initializing ...");
        this.context = new EngineContext(this.config);
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine was initialized.");
    }

    @Override
    public void start() {
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine is starting ...");
        this.run(this.context);
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine is started.");
    }

    @Override
    public void stop() {
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine is stopping ...");
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine was stopped.");
    }

    @Override
    public void destroy() {
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine is destroying ...");
        logger.info(FileConstant.logger.marker.FATAL, "The file-engine was destroyed.");
    }

    public void run(EngineContext context) {

        // 执行多个文件处理前
        if (this.executeHandlers(context, this.beforeFilesExecuteHandlers)) return;

        // 执行多个文件处理
        List<FileContext> fileContextList = context.getFileContextList();
        if (fileContextList == null) {
            throw new NullPointerException("The context-list for actuate file is null.");
        }
        for (FileContext fileContext : fileContextList) {
            try {
                this.actuator.actuate(fileContext);
            } catch (Throwable e) {
                if (!context.continueOnExceptionForLoopFile()) {
                    throw e;
                } else {
                    logger.warn(e.getMessage(), e);
                }
            }
        }

        // 执行多个文件处理后
        if (this.executeHandlers(context, this.afterFilesExecuteHandlers)) return;

        // 最后处理
        logger.error(FileConstant.logger.marker.FATAL, "The file-engine is run-done.");
    }

    // 返回是否退出
    private boolean executeHandlers(EngineContext context, List<IHandler<EngineContext>> handlers) {
        if (handlers == null) {
            return false;
        }
        for (IHandler<EngineContext> handler : handlers) {
            try {
                if (!handler.handle(context)) {
                    return true;
                }
            } catch (Throwable e) {
                if (!context.continueOnExceptionForLoopFilesHandler()) {
                    throw e;
                } else {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return false;
    }
}
