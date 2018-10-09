/**
 * Copyright (c) 2018, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.ud.exception
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 16:24
 */
package net.lizhaoweb.spring.file.ud.exception;

/**
 * <H1>异常 - 文件传输后</H1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2018年10月09日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileAfterTransferException extends FileTransferException {

    /**
     * {@inheritDoc}
     */
    public FileAfterTransferException() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public FileAfterTransferException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public FileAfterTransferException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public FileAfterTransferException(Throwable cause) {
        super(cause);
    }
}
