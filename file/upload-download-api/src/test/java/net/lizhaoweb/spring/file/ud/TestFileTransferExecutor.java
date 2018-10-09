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
            public void before(FileTransferContext context) {
                System.out.println("up-handler-before-1");
            }

            @Override
            public void after(FileTransferContext context) {
                System.out.println("up-handler-after-1");
            }
        }, new IFileTransferHandler() {
            @Override
            public void before(FileTransferContext context) {
                System.out.println("up-handler-before-2");
            }

            @Override
            public void after(FileTransferContext context) {
                System.out.println("up-handler-after-2");
            }
        });

        IFileTransferExecutor executor = new FileTransferExecutor();
        executor.upload(context);
    }

    @Test
    public void download() {
        FileTransferContext context = new FileTransferContext();
        context.addDownloadHandlers(new IFileTransferHandler() {
            @Override
            public void before(FileTransferContext context) {
                System.out.println("down-handler-before-1");
            }

            @Override
            public void after(FileTransferContext context) {
                System.out.println("down-handler-after-1");
            }
        }, new IFileTransferHandler() {
            @Override
            public void before(FileTransferContext context) {
                System.out.println("down-handler-before-2");
            }

            @Override
            public void after(FileTransferContext context) {
                System.out.println("down-handler-after-2");
            }
        });

        IFileTransferExecutor executor = new FileTransferExecutor();
        executor.download(context);
    }
}
