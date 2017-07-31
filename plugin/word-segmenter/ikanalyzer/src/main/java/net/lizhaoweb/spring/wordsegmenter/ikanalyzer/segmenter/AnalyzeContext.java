/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter.ikanalyzer
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 13:46
 */
package net.lizhaoweb.spring.wordsegmenter.ikanalyzer.segmenter;

import lombok.Getter;
import lombok.Setter;
import net.lizhaoweb.spring.wordsegmenter.config.IConfiguration;
import net.lizhaoweb.spring.wordsegmenter.ikanalyzer.dictionary.Dictionary;
import net.lizhaoweb.spring.wordsegmenter.ikanalyzer.util.CharacterUtil;
import net.lizhaoweb.spring.wordsegmenter.model.Lexeme;
import net.lizhaoweb.spring.wordsegmenter.model.LexemePath;
import net.lizhaoweb.spring.wordsegmenter.model.LexemeType;
import net.lizhaoweb.spring.wordsegmenter.model.QuickSortSet;
import net.lizhaoweb.spring.wordsegmenter.segmenter.IAnalyzeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * <h1>实现 - 分词器上下文</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
@Component
public class AnalyzeContext implements IAnalyzeContext {

    //默认缓冲区大小
    private static final int BUFF_SIZE = 4096;
    //缓冲区耗尽的临界值
    private static final int BUFF_EXHAUST_CRITICAL = 100;

    //原始分词结果集合，未经歧义处理
    @Setter
    private QuickSortSet orgLexemes;

    //分词器配置项
    @Setter
    @Autowired
    private IConfiguration cfg;

    //字符窜读取缓冲
    @Getter
    private char[] segmentBuff;
    //字符类型数组
    private int[] charTypes;

    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader起始位置的位移
    @Getter
    private int bufferOffset;
    //当前缓冲区位置指针
    @Getter
    private int cursor;
    //最近一次读入的,可处理的字串长度
    private int available;

    //子分词器锁
    //该集合非空，说明有子分词器在占用segmentBuff
    private Set<String> buffLocker;
    //LexemePath位置索引表
    private Map<Integer, LexemePath> pathMap;
    //最终分词结果集
    private LinkedList<Lexeme> results;

    public AnalyzeContext() {
//        this.cfg = cfg;
//        this.orgLexemes = new QuickSortSet();
        this.segmentBuff = new char[BUFF_SIZE];
        this.charTypes = new int[BUFF_SIZE];
        this.buffLocker = new HashSet<String>();
        this.pathMap = new HashMap<Integer, LexemePath>();
        this.results = new LinkedList<Lexeme>();
    }

    @Override
    public char getCurrentChar() {
        return this.segmentBuff[this.cursor];
    }

    @Override
    public int getCurrentCharType() {
        return this.charTypes[this.cursor];
    }

    /**
     * 根据context的上下文情况，填充segmentBuff
     *
     * @param reader 阅读器
     * @return 返回待分析的（有效的）字串长度
     * @throws IOException 输入输出异常
     */
    @Override
    public int fillBuffer(Reader reader) throws IOException {
        int readCount = 0;
        if (this.bufferOffset == 0) {
            //首次读取reader
            readCount = reader.read(segmentBuff);
        } else {
            int offset = this.available - this.cursor;
            if (offset > 0) {
                //最近一次读取的>最近一次处理的，将未处理的字串拷贝到segmentBuff头部
                System.arraycopy(this.segmentBuff, this.cursor, this.segmentBuff, 0, offset);
                readCount = offset;
            }
            //继续读取reader ，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余的部分
            readCount += reader.read(this.segmentBuff, offset, BUFF_SIZE - offset);
        }
        //记录最后一次从Reader中读入的可用字符长度
        this.available = readCount;
        //重置当前指针
        this.cursor = 0;
        return readCount;
    }

    /**
     * 初始化buff指针，处理第一个字符
     */
    @Override
    public void initCursor() {
        this.cursor = 0;
        this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
        this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
    }

    /**
     * 指针+1
     * 成功返回 true； 指针已经到了buff尾部，不能前进，返回false
     * 并处理当前字符
     */
    @Override
    public boolean moveCursor() {
        if (this.cursor < this.available - 1) {
            this.cursor++;
            this.segmentBuff[this.cursor] = CharacterUtil.regularize(this.segmentBuff[this.cursor]);
            this.charTypes[this.cursor] = CharacterUtil.identifyCharType(this.segmentBuff[this.cursor]);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置当前segmentBuff为锁定状态
     * 加入占用segmentBuff的子分词器名称，表示占用segmentBuff
     *
     * @param segmenterTag 分词器标签
     */
    @Override
    public void lockBuffer(String segmenterTag) {
        this.buffLocker.add(segmenterTag);
    }

    /**
     * 移除指定的子分词器名，释放对segmentBuff的占用
     *
     * @param segmenterTag 分词器标签
     */
    @Override
    public void unlockBuffer(String segmenterTag) {
        this.buffLocker.remove(segmenterTag);
    }

    /**
     * 只要buffLocker中存在segmenterName
     * 则buffer被锁定
     *
     * @return boolean 缓冲去是否被锁定
     */
    @Override
    public boolean isBufferLocked() {
        return this.buffLocker.size() > 0;
    }

    /**
     * 判断当前segmentBuff是否已经用完
     * 当前执针cursor移至segmentBuff末端this.available - 1
     *
     * @return boolean
     */
    @Override
    public boolean isBufferConsumed() {
        return this.cursor == this.available - 1;
    }

    /**
     * 判断segmentBuff是否需要读取新数据
     * <p>
     * 满足一下条件时，
     * 1.available == BUFF_SIZE 表示buffer满载
     * 2.buffIndex &lt; available - 1 &amp;&amp; buffIndex &gt; available - BUFF_EXHAUST_CRITICAL表示当前指针处于临界区内
     * 3.!context.isBufferLocked()表示没有segmenter在占用buffer
     * 要中断当前循环（buffer要进行移位，并再读取数据的操作）
     *
     * @return boolean
     */
    @Override
    public boolean needRefillBuffer() {
        return this.available == BUFF_SIZE
                && this.cursor < this.available - 1
                && this.cursor > this.available - BUFF_EXHAUST_CRITICAL
                && !this.isBufferLocked();
    }

    /**
     * 累计当前的segmentBuff相对于reader起始位置的位移
     */
    @Override
    public void markBufferOffset() {
        this.bufferOffset += this.cursor;
    }

    /**
     * 向分词结果集添加词元
     *
     * @param lexeme lexeme
     */
    @Override
    public void addLexeme(Lexeme lexeme) {
        this.orgLexemes.addLexeme(lexeme);
    }

    /**
     * 添加分词结果路径
     * 路径起始位置 ---&gt; 路径 映射表
     *
     * @param path Lexeme 路径
     */
    @Override
    public void addLexemePath(LexemePath path) {
        if (path != null) {
            this.pathMap.put(path.getPathBegin(), path);
        }
    }

    /**
     * 返回原始分词结果
     *
     * @return QuickSortSet
     */
    @Override
    public QuickSortSet getOrgLexemes() {
        return this.orgLexemes;
    }

    /**
     * 推送分词结果到结果集合
     * 1.从buff头部遍历到this.cursor已处理位置
     * 2.将map中存在的分词结果推入results
     * 3.将map中不存在的CJDK字符以单字方式推入results
     */
    @Override
    public void outputToResult() {
        int index = 0;
        for (; index <= this.cursor; ) {
            //跳过非CJK字符
            if (CharacterUtil.CHAR_USELESS == this.charTypes[index]) {
                index++;
                continue;
            }
            //从pathMap找出对应index位置的LexemePath
            LexemePath path = this.pathMap.get(index);
            if (path != null) {
                //输出LexemePath中的lexeme到results集合
                Lexeme l = path.pollFirst();
                while (l != null) {
                    this.results.add(l);
                    //将index移至lexeme后
                    index = l.getBegin() + l.getLength();
                    l = path.pollFirst();
                    if (l != null) {
                        //输出path内部，词元间遗漏的单字
                        for (; index < l.getBegin(); index++) {
                            this.outputSingleCJK(index);
                        }
                    }
                }
            } else {//pathMap中找不到index对应的LexemePath
                //单字输出
                this.outputSingleCJK(index);
                index++;
            }
        }
        //清空当前的Map
        this.pathMap.clear();
    }

    /**
     * 对CJK字符进行单字输出
     *
     * @param index 索引
     */
    private void outputSingleCJK(int index) {
        if (CharacterUtil.CHAR_CHINESE == this.charTypes[index]) {
            Lexeme singleCharLexeme = new Lexeme(this.bufferOffset, index, 1, LexemeType.CNCHAR);
            this.results.add(singleCharLexeme);
        } else if (CharacterUtil.CHAR_OTHER_CJK == this.charTypes[index]) {
            Lexeme singleCharLexeme = new Lexeme(this.bufferOffset, index, 1, LexemeType.OTHER_CJK);
            this.results.add(singleCharLexeme);
        }
    }

    /**
     * 返回lexeme
     * <p>
     * 同时处理合并
     *
     * @return Lexeme
     */
    @Override
    public Lexeme getNextLexeme() {
        //从结果集取出，并移除第一个Lexme
        Lexeme result = this.results.pollFirst();
        while (result != null) {
            //数量词合并
            this.compound(result);
            if (Dictionary.getSingleton().isStopWord(this.segmentBuff, result.getBegin(), result.getLength())) {
                //是停止词继续取列表的下一个
                result = this.results.pollFirst();
            } else {
                //不是停止词, 生成lexeme的词元文本,输出
                result.setLexemeText(String.valueOf(segmentBuff, result.getBegin(), result.getLength()));
                break;
            }
        }
        return result;
    }

    /**
     * 重置分词上下文状态
     */
    @Override
    public void reset() {
        this.buffLocker.clear();
        this.orgLexemes = new QuickSortSet();
        this.available = 0;
        this.bufferOffset = 0;
        this.charTypes = new int[BUFF_SIZE];
        this.cursor = 0;
        this.results.clear();
        this.segmentBuff = new char[BUFF_SIZE];
        this.pathMap.clear();
    }

    /**
     * 组合词元
     *
     * @param result Lexeme
     */
    private void compound(Lexeme result) {
        if (!this.cfg.useSmart()) {
            return;
        }
        //数量词合并处理
        if (!this.results.isEmpty()) {

            if (LexemeType.ARABIC == result.getLexemeType()) {
                Lexeme nextLexeme = this.results.peekFirst();
                boolean appendOk = false;
                if (LexemeType.CNUM == nextLexeme.getLexemeType()) {
                    //合并英文数词+中文数词
                    appendOk = result.append(nextLexeme, LexemeType.CNUM);
                } else if (LexemeType.COUNT == nextLexeme.getLexemeType()) {
                    //合并英文数词+中文量词
                    appendOk = result.append(nextLexeme, LexemeType.CQUAN);
                }
                if (appendOk) {
                    //弹出
                    this.results.pollFirst();
                }
            }

            //可能存在第二轮合并
            if (LexemeType.CNUM == result.getLexemeType() && !this.results.isEmpty()) {
                Lexeme nextLexeme = this.results.peekFirst();
                boolean appendOk = false;
                if (LexemeType.COUNT == nextLexeme.getLexemeType()) {
                    //合并中文数词+中文量词
                    appendOk = result.append(nextLexeme, LexemeType.CQUAN);
                }
                if (appendOk) {
                    //弹出
                    this.results.pollFirst();
                }
            }
        }
    }
}
