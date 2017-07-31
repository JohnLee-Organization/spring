/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 11:44
 */
package net.lizhaoweb.spring.wordsegmenter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

/**
 * <h1>模型 - 词元</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
@NoArgsConstructor
@Component
public class Lexeme implements Comparable<Lexeme> {

    /**
     * 词元的起始位移
     */
    @Setter(value = PACKAGE)
    @Getter(value = PACKAGE)
    private int offset;

    /**
     * 词元的相对起始位置
     */
    @Setter(value = PACKAGE)
    @Getter(value = PUBLIC)
    private int begin;

    /**
     * 词元的长度
     */
    @Getter(value = PUBLIC)
    private int length;

    /**
     * 词元类型
     */
    @Setter(value = PACKAGE)
    @Getter(value = PUBLIC)
    private LexemeType lexemeType;

    /**
     * 词元文本
     */
    private String lexemeText;

    /**
     * 构造函数
     *
     * @param offset     词元的起始位移
     * @param begin      词元的相对起始位置
     * @param length     词元的长度
     * @param lexemeType 词元类型
     */
    public Lexeme(int offset, int begin, int length, String lexemeType) {
        System.out.println("======================" + lexemeType);
        // this(offset, begin, length, LexemeType.fromValue(lexemeType));
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;

        this.lexemeType = LexemeType.fromValue(lexemeType);
    }

    /**
     * 构造函数
     *
     * @param offset     词元的起始位移
     * @param begin      词元的相对起始位置
     * @param length     词元的长度
     * @param lexemeType 词元类型
     */
    public Lexeme(int offset, int begin, int length, LexemeType lexemeType) {
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
        this.lexemeType = lexemeType;
    }

    /**
     * 设置词元的字符长度
     *
     * @param length 词元的字符长度
     */
    public void setLength(int length) {
        if (this.length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
    }

    /**
     * 设置词元文本内容
     *
     * @param lexemeText 词元文本
     */
    public void setLexemeText(String lexemeText) {
        if (lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }
    }

    /**
     * 获取词元的文本内容
     *
     * @return String
     */
    public String getLexemeText() {
        if (lexemeText == null) {
            return "";
        }
        return lexemeText;
    }

    /**
     * 获取词元在文本中的起始位置
     *
     * @return int
     */
    public int getBeginPosition() {
        return offset + begin;
    }

    /**
     * 获取词元在文本中的结束位置
     *
     * @return int
     */
    public int getEndPosition() {
        return offset + begin + length;
    }

    /**
     * 获取词元类型标示字符串
     *
     * @return String
     */
    public String getLexemeTypeString() {
        switch (lexemeType) {
            case ENGLISH:
                return LexemeType.ENGLISH.getName();
            case ARABIC:
                return LexemeType.ARABIC.getName();
            case LETTER:
                return LexemeType.LETTER.getName();
            case CNWORD:
                return LexemeType.CNWORD.getName();
            case CNCHAR:
                return LexemeType.CNCHAR.getName();
            case OTHER_CJK:
                return LexemeType.OTHER_CJK.getName();
            case COUNT:
                return LexemeType.COUNT.getName();
            case CNUM:
                return LexemeType.CNUM.getName();
            case CQUAN:
                return LexemeType.CQUAN.getName();
            default:
                return LexemeType.UNKNOWN.getName();
        }
    }

    /**
     * 合并两个相邻的词元
     *
     * @param l          词元
     * @param lexemeType 词元类型
     * @return boolean 词元是否成功合并
     */
    public boolean append(Lexeme l, String lexemeType) {
        return this.append(l, LexemeType.fromValue(lexemeType));
    }

    /**
     * 合并两个相邻的词元
     *
     * @param l          词元
     * @param lexemeType 词元类型
     * @return boolean 词元是否成功合并
     */
    public boolean append(Lexeme l, LexemeType lexemeType) {
        if (l != null && this.getEndPosition() == l.getBeginPosition()) {
            this.length += l.getLength();
            this.lexemeType = lexemeType;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 词元哈希编码算法
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int absBegin = getBeginPosition();
        int absEnd = getEndPosition();
        return (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }

    /**
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     *
     * @see java.lang.Object#equals(Object o)
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Lexeme) {
            Lexeme other = (Lexeme) o;
            if (this.offset == other.getOffset()
                    && this.begin == other.getBegin()
                    && this.length == other.getLength()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 词元在排序集合中的比较算法
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Lexeme other) {
        //起始位置优先
        if (this.begin < other.getBegin()) {
            return -1;
        } else if (this.begin == other.getBegin()) {
            //词元长度优先
            if (this.length > other.getLength()) {
                return -1;
            } else if (this.length == other.getLength()) {
                return 0;
            } else {//this.length < other.getLength()
                return 1;
            }
        } else {//this.begin > other.getBegin()
            return 1;
        }
    }

    /**
     * 序列化
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.getBeginPosition()).append("-").append(this.getEndPosition());
        strbuf.append(" : ").append(this.lexemeText).append(" : \t");
        strbuf.append(this.getLexemeTypeString());
        return strbuf.toString();
    }
}
