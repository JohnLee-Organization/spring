/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.ikanalyzer.lucene
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:33
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.lucene;

import lombok.Setter;
import net.lizhaoweb.spring.wordsegmenter.analyzer.IScheduler;
import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

/**
 * <h1>IK分词器 Lucene Tokenizer适配器类</h1>
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
public final class IKTokenizer extends Tokenizer {

    //IK分词器实现
    @Setter
    private IScheduler ikSegmentScheduler;

    //词元文本属性
    private final CharTermAttribute termAtt;
    //词元位移属性
    private final OffsetAttribute offsetAtt;
    //词元分类属性（该属性分类参考org.wltea.analyzer.core.Lexeme中的分类常量）
    private final TypeAttribute typeAtt;
    //记录最后一个词元的结束位置
    private int endPosition;

    /**
     * Lucene 4.0 Tokenizer适配器类构造函数
     *
     * @param in 阅读器
     */
    public IKTokenizer(Reader in) {
        super(in);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
    }

    /**
     * Lucene 4.0 Tokenizer适配器类构造函数
     *
     * @param useSmart 是否智能
     */
    public void setUseSmart(boolean useSmart) {
        this.ikSegmentScheduler.setUseSmart(input, useSmart);
    }

    /**
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        //清除所有的词元属性
        clearAttributes();
        Lexeme nextLexeme = ikSegmentScheduler.next();
        if (nextLexeme != null) {
            //将Lexeme转成Attributes
            //设置词元文本
            termAtt.append(nextLexeme.getLexemeText());
            //设置词元长度
            termAtt.setLength(nextLexeme.getLength());
            //设置词元位移
            offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            //记录分词的最后位置
            endPosition = nextLexeme.getEndPosition();
            //记录词元分类
            typeAtt.setType(nextLexeme.getLexemeTypeString());
            //返会true告知还有下个词元
            return true;
        }
        //返会false告知词元输出完毕
        return false;
    }

    /**
     * @see org.apache.lucene.analysis.Tokenizer#reset()
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        ikSegmentScheduler.reset(input);
    }

    /**
     * @see org.apache.lucene.analysis.Tokenizer#end()
     */
    @Override
    public final void end() {
        // set final offset
        int finalOffset = correctOffset(this.endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
    }
}
