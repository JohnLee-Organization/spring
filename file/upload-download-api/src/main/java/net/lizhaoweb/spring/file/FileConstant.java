/**
 * Copyright (c) 2019, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.file
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 17:14
 */
package net.lizhaoweb.spring.file;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @EMAIL 404644381@qq.com
 * @notes Created on 2019年08月13日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface FileConstant {
    public static interface logger {
        public static interface marker {
            public static Marker TRACE = MarkerFactory.getMarker("TRACE");
            public static Marker DEBUG = MarkerFactory.getMarker("DEBUG");
            public static Marker INFO = MarkerFactory.getMarker("INFO");
            public static Marker WARN = MarkerFactory.getMarker("WARN");
            public static Marker ERROR = MarkerFactory.getMarker("ERROR");
            public static Marker FATAL = MarkerFactory.getMarker("FATAL");
        }
    }

    public static interface EngineContextFields {
        public static final String CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE = EngineContext.class.getName() + ".continueOnExceptionForLoopFile";
        public static final String CONTINUE_ON_EXCEPTION_FOR_LOOP_FILES_HANDLER = EngineContext.class.getName() + ".continueOnExceptionForLoopFilesHandler";
    }

    public static interface FileActuatorFields {
        public static final String CONTINUE_ON_EXCEPTION_FOR_LOOP_FILE_HANDLER = FileActuator.class.getName() + ".continueOnExceptionForLoopFileHandler";
    }
}
