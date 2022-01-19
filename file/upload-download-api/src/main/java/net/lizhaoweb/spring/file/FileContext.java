/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 11:16
 */
package net.lizhaoweb.spring.file;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2019年08月07日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class FileContext extends AbstractContext<FileConfig> {

    public FileContext(FileConfig config) {
        super(config);
    }

    public void continueOnExceptionForLoopFileHandler(boolean value) {
        this.setField(FileConstant.FileActuatorFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE_HANDLER, value);
    }

    public boolean continueOnExceptionForLoopFileHandler() {
        return this.getBooleanField(FileConstant.FileActuatorFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE_HANDLER, false);
    }
}
