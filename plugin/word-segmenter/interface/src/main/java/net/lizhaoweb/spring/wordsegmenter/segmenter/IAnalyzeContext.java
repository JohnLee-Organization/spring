/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:18
 */
package net.lizhaoweb.spring.wordsegmenter.segmenter;

import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import net.lizhaoweb.spring.wordsegmenter.model.LexemePath;
import net.lizhaoweb.spring.wordsegmenter.model.QuickSortSet;

import java.io.IOException;
import java.io.Reader;

/**
 * <h1>接口 - 分词器上下文</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IAnalyzeContext {

    /**
     * 获取当前缓冲区位置指针。
     *
     * @return int
     */
    int getCursor();

    /**
     * 字符窜读取缓冲区。
     *
     * @return char[]
     */
    char[] getSegmentBuff();

    /**
     * 获取当前字符。
     *
     * @return char
     */
    char getCurrentChar();

    /**
     * 获取当前字符类型
     *
     * @return int
     */
    int getCurrentCharType();

    /**
     * 记录Reader内已分析的字串总长度
     * 在分多段分析词元时，该变量累计当前的segmentBuff相对于reader起始位置的位移
     *
     * @return int
     */
    int getBufferOffset();

    /**
     * 根据context的上下文情况，填充segmentBuff
     *
     * @param reader 阅读器
     * @return 返回待分析的（有效的）字串长度
     * @throws IOException 输入输出异常
     */
    int fillBuffer(Reader reader) throws IOException;

    /**
     * 初始化buff指针，处理第一个字符
     */
    void initCursor();

    /**
     * 指针+1
     * 成功返回 true； 指针已经到了buff尾部，不能前进，返回false
     * 并处理当前字符
     *
     * @return boolean
     */
    boolean moveCursor();

    /**
     * 设置当前segmentBuff为锁定状态
     * 加入占用segmentBuff的子分词器名称，表示占用segmentBuff
     *
     * @param segmenterTag 分词器标签
     */
    void lockBuffer(String segmenterTag);

    /**
     * 移除指定的子分词器名，释放对segmentBuff的占用
     *
     * @param segmenterTag 分词器标签
     */
    void unlockBuffer(String segmenterTag);

    /**
     * 只要buffLocker中存在segmenterName
     * 则buffer被锁定
     *
     * @return boolean 缓冲去是否被锁定
     */
    boolean isBufferLocked();

    /**
     * 判断当前segmentBuff是否已经用完
     * 当前执针cursor移至segmentBuff末端this.available - 1
     *
     * @return boolean
     */
    boolean isBufferConsumed();

    /**
     * 判断segmentBuff是否需要读取新数据
     * <p>
     * 满足一下条件时，<br>
     * <pre>
     * 1.available == BUFF_SIZE 表示buffer满载
     * 2.buffIndex &lt; available - 1 &amp;&amp; buffIndex &gt; available - BUFF_EXHAUST_CRITICAL 表示当前指针处于临界区内
     * 3.!context.isBufferLocked() 表示没有 segmenter 在占用 buffer
     * </pre>
     * 要中断当前循环（buffer要进行移位，并再读取数据的操作）
     *
     * @return boolean
     */
    boolean needRefillBuffer();

    /**
     * 累计当前的segmentBuff相对于reader起始位置的位移
     */
    void markBufferOffset();

    /**
     * 向分词结果集添加词元
     *
     * @param lexeme 词元
     */
    void addLexeme(Lexeme lexeme);

    /**
     * 添加分词结果路径
     * 路径起始位置 ---&lt; 路径 映射表
     *
     * @param path 词元链(路径)
     */
    void addLexemePath(LexemePath path);

    /**
     * 返回原始分词结果
     *
     * @return QuickSortSet
     */
    QuickSortSet getOrgLexemes();

    /**
     * 推送分词结果到结果集合
     * 1.从buff头部遍历到this.cursor已处理位置
     * 2.将map中存在的分词结果推入results
     * 3.将map中不存在的CJDK字符以单字方式推入results
     */
    void outputToResult();

    /**
     * 返回lexeme
     * <p>
     * 同时处理合并
     *
     * @return Lexeme
     */
    Lexeme getNextLexeme();

    /**
     * 重置分词上下文状态
     */
    void reset();
}
