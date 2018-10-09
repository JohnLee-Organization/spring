/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.ud
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:53
 */
package net.lizhaoweb.spring.file.ud;

import net.lizhaoweb.spring.file.ud.exception.FileAfterTransferException;
import net.lizhaoweb.spring.file.ud.exception.FileBeforeTransferException;
import net.lizhaoweb.spring.file.ud.model.FileTransferContext;

/**
 * <h1>执行器 [实现] - 文件传输</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年10月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileTransferExecutor implements IFileTransferExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(FileTransferContext context) {
        IFileTransferHandler[] handlers = context.getUploadHandlers();
        this.executeBefore(context, handlers);
        //TODO 上传逻辑
        this.executeAfter(context, handlers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void download(FileTransferContext context) {
        IFileTransferHandler[] handlers = context.getDownloadHandlers();
        this.executeBefore(context, handlers);
        //TODO 下载逻辑
        this.executeAfter(context, handlers);
    }

    // 执行前运行
    private void executeBefore(FileTransferContext context, IFileTransferHandler[] handlers) {
        if (handlers != null && handlers.length > 0) {
            for (IFileTransferHandler handler : handlers) {
                try {
                    handler.before(context);
                } catch (Exception e) {
                    throw new FileBeforeTransferException(e);
                }
            }
        }
    }

    // 执行后运行
    private void executeAfter(FileTransferContext context, IFileTransferHandler[] handlers) {
        if (handlers != null && handlers.length > 0) {
            for (int index = handlers.length - 1; index > -1; index--) {
                try {
                    IFileTransferHandler handler = handlers[index];
                    handler.after(context);
                } catch (Exception e) {
                    throw new FileAfterTransferException(e);
                }
            }
        }
    }

}
