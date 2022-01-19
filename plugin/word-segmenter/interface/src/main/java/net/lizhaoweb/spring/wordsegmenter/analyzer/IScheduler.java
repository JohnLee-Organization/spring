/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.analyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 17:02
 */
package net.lizhaoweb.spring.wordsegmenter.analyzer;

import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;

import java.io.IOException;
import java.io.Reader;

/**
 * <h1>接口 - 调度器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IScheduler {

//    /**
//     * 初始化
//     */
//    void init();

    /**
     * 分词，获取下一个词元
     *
     * @return Lexeme 词元对象
     * @throws IOException 输入输出异常
     */
    Lexeme next() throws IOException;

    /**
     * 重置分词器到初始状态
     *
     * @param input 阅读器
     */
    void reset(Reader input);

//    /**
//     * 设置配置
//     */
//    void setConfiguration(IConfiguration configuration);

    void setUseSmart(Reader input, boolean useSmart);
}
