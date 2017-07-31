/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:32
 */
package net.lizhaoweb.spring.wordsegmenter.segmenter;

import java.io.IOException;

/**
 * <h1>接口 - 分词器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface ISegmenter {

    /**
     * 从分析器读取下一个可能分解的词元对象
     *
     * @param context 分词算法上下文
     * @throws IOException 输入输出异常
     */
    void analyze(IAnalyzeContext context) throws IOException;

    /**
     * 重置子分析器状态
     */
    void reset();
}
