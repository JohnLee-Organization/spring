/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 16:16
 */
package net.lizhaoweb.spring.file;

import lombok.Setter;

import java.util.List;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @email 404644381@qq.com
 * @notes Created on 2019年08月13日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public class EngineContext extends AbstractContext<EngineConfig> {

    @Setter
    private IConverter<EngineContext, List<FileContext>> fileContextListConverter;

    public EngineContext(EngineConfig config) {
        super(config);
    }

    public List<FileContext> getFileContextList() {
        if (this.fileContextListConverter == null) {
            throw new NullPointerException("The converter for file-context-list is null.");
        }
        return this.fileContextListConverter.convert(this);
    }

    public void continueOnExceptionForLoopFile(boolean value) {
        this.setField(FileConstant.EngineContextFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE, value);
    }

    public boolean continueOnExceptionForLoopFile() {
        return this.getBooleanField(FileConstant.EngineContextFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE, false);
    }

    public void continueOnExceptionForLoopFilesHandler(boolean value) {
        this.setField(FileConstant.EngineContextFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILES_HANDLER, value);
    }

    public boolean continueOnExceptionForLoopFilesHandler() {
        return this.getBooleanField(FileConstant.EngineContextFields.CONTINUE_ON_EXCEPTION_FOR_LOOP_FILES_HANDLER, false);
    }
}
