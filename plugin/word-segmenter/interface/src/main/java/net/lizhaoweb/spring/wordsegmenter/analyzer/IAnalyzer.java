/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.analyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:48
 */
package net.lizhaoweb.spring.wordsegmenter.analyzer;

import org.apache.lucene.analysis.TokenStream;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * <h1>接口 - 分析仪</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public interface IAnalyzer extends Closeable {

    void setUseSmart(boolean useSmart);

    TokenStream tokenStream(final String fieldName, final Reader reader) throws IOException;
}
