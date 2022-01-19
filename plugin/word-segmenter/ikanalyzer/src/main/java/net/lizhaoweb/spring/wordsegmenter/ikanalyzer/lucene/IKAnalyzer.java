/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.ikanalyzer.lucene
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:31
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.lucene;

import lombok.Setter;
import net.lizhaoweb.spring.wordsegmenter.analyzer.IAnalyzer;
import net.lizhaoweb.spring.wordsegmenter.analyzer.IScheduler;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Reader;

/**
 * <h1>IK分词器，Lucene Analyzer接口实现</h1>
 * 兼容Lucene 4.0版本
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
@Component
public final class IKAnalyzer extends Analyzer implements IAnalyzer {

    @Setter
    @Autowired
    private IScheduler ikSegmentScheduler;

    @Setter
    private boolean useSmart;

    /**
     * IK分词器Lucene  Analyzer接口实现类
     * <p>
     * 默认细粒度切分算法
     */
    public IKAnalyzer() {
        this.setUseSmart(false);
    }

    public boolean useSmart() {
        return useSmart;
    }

    /**
     * 重载Analyzer接口，构造分词组件
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName, final Reader in) {
        Tokenizer ikTokenizer = new IKTokenizer(in);
        if (ikTokenizer instanceof IKTokenizer) {
            IKTokenizer _ikTokenizer = (IKTokenizer) ikTokenizer;
            _ikTokenizer.setIkSegmentScheduler(ikSegmentScheduler);
            _ikTokenizer.setUseSmart(this.useSmart());
        }
        return new TokenStreamComponents(ikTokenizer);
    }
}
