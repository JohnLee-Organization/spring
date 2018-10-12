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
public abstract class AbstractFileTransferExecutor implements IFileTransferExecutor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean upload(FileTransferContext context) {
        IFileTransferHandler[] handlers = context.getUploadHandlers();
        boolean execNext = true;

        if (execNext) {
            execNext = this.executeBefore(context, handlers);
        }
        if (execNext) {
            execNext = this.executeUpload(context);
        }
        if (execNext) {
            execNext = this.executeAfter(context, handlers);
        }
        return execNext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean download(FileTransferContext context) {
        IFileTransferHandler[] handlers = context.getDownloadHandlers();

        boolean execNext = true;

        if (execNext) {
            execNext = this.executeBefore(context, handlers);
        }
        if (execNext) {
            execNext = this.executeDownload(context);
        }
        if (execNext) {
            execNext = this.executeAfter(context, handlers);
        }
        return execNext;
    }

    /**
     * 执行上传
     *
     * @param context 文件传输上下文
     */
    protected abstract boolean executeUpload(FileTransferContext context);

    /**
     * 执行下载
     *
     * @param context 文件传输上下文
     */
    protected abstract boolean executeDownload(FileTransferContext context);

    // 执行前运行
    private boolean executeBefore(FileTransferContext context, IFileTransferHandler[] handlers) {
        boolean execNext = true;
        if (handlers != null && handlers.length > 0) {
            for (IFileTransferHandler handler : handlers) {
                try {
                    if (execNext) {
                        execNext = handler.before(context);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    throw new FileBeforeTransferException(e);
                }
            }
        }
        return execNext;
    }

    // 执行后运行
    private boolean executeAfter(FileTransferContext context, IFileTransferHandler[] handlers) {
        boolean execNext = true;
        if (handlers != null && handlers.length > 0) {
            for (int index = handlers.length - 1; index > -1; index--) {
                try {
                    if (execNext) {
                        IFileTransferHandler handler = handlers[index];
                        execNext = handler.after(context);
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    throw new FileAfterTransferException(e);
                }
            }
        }
        return execNext;
    }

}
