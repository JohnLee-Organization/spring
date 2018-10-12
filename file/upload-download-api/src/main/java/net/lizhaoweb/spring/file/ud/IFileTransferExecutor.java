/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.ud
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:02
 */
package net.lizhaoweb.spring.file.ud;

import net.lizhaoweb.spring.file.ud.model.FileTransferContext;

/**
 * <h1>执行器 [接口] - 文件传输</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年07月03日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IFileTransferExecutor {

    /**
     * 上传文件
     *
     * @param context 文件传输上下文
     * @return boolean
     */
    boolean upload(FileTransferContext context);

    /**
     * 下载文件
     *
     * @param context 文件传输上下文
     * @return boolean
     */
    boolean download(FileTransferContext context);
}
