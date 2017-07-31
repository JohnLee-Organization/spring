/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter.ikanalyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 15:10
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer;

import lombok.NoArgsConstructor;
import lombok.Setter;
import net.lizhaoweb.spring.wordsegmenter.analyzer.IScheduler;
import net.lizhaoweb.spring.wordsegmenter.config.IConfiguration;
import net.lizhaoweb.spring.wordsegmenter.ikanalyzer.dictionary.Dictionary;
import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import net.lizhaoweb.spring.wordsegmenter.segmenter.IAnalyzeContext;
import net.lizhaoweb.spring.wordsegmenter.segmenter.IArbitrator;
import net.lizhaoweb.spring.wordsegmenter.segmenter.ISegmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * <h1>实现 - IK分词调度器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@NoArgsConstructor
@Component
public class IKSegmentScheduler implements IScheduler {

    //字符窜reader
    private Reader input;

    //分词器配置项
    @Autowired
    private IConfiguration configuration;

    //分词器上下文
    @Autowired
    @Setter
    private IAnalyzeContext context;

    //分词处理器列表
    @Autowired
    @Setter
    private List<ISegmenter> segmenters;

    //分词歧义裁决器
    @Autowired
    @Setter
    private IArbitrator arbitrator;

    /**
     * @param input    阅读器
     * @param useSmart 为true，使用智能分词策略
     *                 <p>
     *                 非智能分词：细粒度输出所有可能的切分结果
     *                 智能分词： 合并数词和量词，对分词结果进行歧义判断
     */
    @Override
    public void setUseSmart(Reader input, boolean useSmart) {
        this.input = input;
        this.configuration.setUseSmart(useSmart);
    }


    /**
     * 初始化
     *
     * @param configuration 配置对象
     */
    public void setConfiguration(IConfiguration configuration) {
        //初始化词典单例
        Dictionary.initial(configuration);
        this.configuration = configuration;
    }

    /**
     * 分词，获取下一个词元
     *
     * @return Lexeme 词元对象
     * @throws IOException 输入输出异常
     */
    @Override
    public synchronized Lexeme next() throws IOException {
        Lexeme lexeme = null;
        while ((lexeme = context.getNextLexeme()) == null) {
            /*
             * 从reader中读取数据，填充buffer
			 * 如果reader是分次读入buffer的，那么buffer要  进行移位处理
			 * 移位处理上次读入的但未处理的数据
			 */
            int available = context.fillBuffer(this.input);
            if (available <= 0) {
                //reader已经读完
                context.reset();
                return null;
            } else {
                //初始化指针
                context.initCursor();
                do {
                    //遍历子分词器
                    for (ISegmenter segmenter : segmenters) {
                        segmenter.analyze(context);
                    }
                    //字符缓冲区接近读完，需要读入新的字符
                    if (context.needRefillBuffer()) {
                        break;
                    }
                    //向前移动指针
                } while (context.moveCursor());
                //重置子分词器，为下轮循环进行初始化
                for (ISegmenter segmenter : segmenters) {
                    segmenter.reset();
                }
            }
            //对分词进行歧义处理
            this.arbitrator.process(context, this.configuration.useSmart());
            //将分词结果输出到结果集，并处理未切分的单个CJK字符
            context.outputToResult();
            //记录本次分词的缓冲区位移
            context.markBufferOffset();
        }
        return lexeme;
    }

    /**
     * 重置分词器到初始状态
     *
     * @param input 阅读器
     */
    @Override
    public synchronized void reset(Reader input) {
        this.input = input;
        context.reset();
        for (ISegmenter segmenter : segmenters) {
            segmenter.reset();
        }
    }
}
