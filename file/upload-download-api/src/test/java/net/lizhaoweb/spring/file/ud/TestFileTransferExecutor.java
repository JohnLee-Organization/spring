/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.ud
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:14
 */
package net.lizhaoweb.spring.file.ud;

import net.lizhaoweb.spring.file.ud.exception.FileTransferException;
import net.lizhaoweb.spring.file.ud.model.FileTransferContext;
import org.junit.Test;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年10月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class TestFileTransferExecutor {

    @Test
    public void upload() {
        FileTransferContext context = new FileTransferContext();
        context.addUploadHandlers(new IFileTransferHandler() {
            @Override
            public boolean before(FileTransferContext context) {
                System.out.println("up-handler-before-1");
                return true;
            }

            @Override
            public boolean after(FileTransferContext context) {
                System.out.println("up-handler-after-1");
                return true;
            }
        }, new IFileTransferHandler() {
            @Override
            public boolean before(FileTransferContext context) {
                System.out.println("up-handler-before-2");
                return true;
            }

            @Override
            public boolean after(FileTransferContext context) {
                System.out.println("up-handler-after-2");
                return true;
            }
        });

        IFileTransferExecutor executor = new AbstractFileTransferExecutor() {
            @Override
            protected boolean executeUpload(FileTransferContext context) {
                System.out.println("up-execute");
                return true;
            }

            @Override
            protected boolean executeDownload(FileTransferContext context) {
                throw new FileTransferException("This method is not supported.");
            }
        };
        executor.upload(context);
        System.out.println();
    }

    @Test
    public void download() {
        FileTransferContext context = new FileTransferContext();
        context.addDownloadHandlers(new IFileTransferHandler() {
            @Override
            public boolean before(FileTransferContext context) {
                System.out.println("down-handler-before-1");
                return true;
            }

            @Override
            public boolean after(FileTransferContext context) {
                System.out.println("down-handler-after-1");
                return true;
            }
        }, new IFileTransferHandler() {
            @Override
            public boolean before(FileTransferContext context) {
                System.out.println("down-handler-before-2");
                return true;
            }

            @Override
            public boolean after(FileTransferContext context) {
                System.out.println("down-handler-after-2");
                return true;
            }
        });

        IFileTransferExecutor executor = new AbstractFileTransferExecutor() {
            @Override
            protected boolean executeUpload(FileTransferContext context) {
                throw new FileTransferException("This method is not supported.");
            }

            @Override
            protected boolean executeDownload(FileTransferContext context) {
                System.out.println("down-execute");
                return true;
            }
        };
        executor.download(context);
        System.out.println();
    }
}
