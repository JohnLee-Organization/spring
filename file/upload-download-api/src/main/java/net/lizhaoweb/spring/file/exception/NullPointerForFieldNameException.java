/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file.exception
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 14:09
 */
package net.lizhaoweb.spring.file.exception;

/**
 * <h1>异常 —— 字段名空指针</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2019年08月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class NullPointerForFieldNameException extends RuntimeException {
    public NullPointerForFieldNameException() {
        super();
    }

    public NullPointerForFieldNameException(String message) {
        super(message);
    }

    public NullPointerForFieldNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullPointerForFieldNameException(Throwable cause) {
        super(cause);
    }
}
