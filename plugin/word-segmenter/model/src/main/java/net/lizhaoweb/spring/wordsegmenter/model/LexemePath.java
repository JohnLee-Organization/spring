/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 13:04
 */
package net.lizhaoweb.spring.wordsegmenter.model;

import lombok.Getter;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

/**
 * <h1>模型 - 词元链(路径)</h1>
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
public class LexemePath extends QuickSortSet implements Comparable<LexemePath> {

    /**
     * 起始位置
     */
    @Getter(value = PUBLIC)
    private int pathBegin;

    /**
     * 结束位置
     */
    @Getter(value = PACKAGE)
    private int pathEnd;

    /**
     * 词元链的有效字符长度
     */
    @Getter(value = PACKAGE)
    private int payloadLength;

    public LexemePath() {
        this.pathBegin = -1;
        this.pathEnd = -1;
        this.payloadLength = 0;
    }

    /**
     * 向LexemePath追加相交的Lexeme
     *
     * @param lexeme 词元
     * @return boolean
     */
    public boolean addCrossLexeme(Lexeme lexeme) {
        if (this.isEmpty()) {
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();
            return true;
        } else if (this.checkCross(lexeme)) {
            this.addLexeme(lexeme);
            if (lexeme.getBegin() + lexeme.getLength() > this.pathEnd) {
                this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            }
            this.payloadLength = this.pathEnd - this.pathBegin;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向LexemePath追加不相交的Lexeme
     *
     * @param lexeme 词元
     * @return boolean
     */
    public boolean addNotCrossLexeme(Lexeme lexeme) {
        if (this.isEmpty()) {
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();
            return true;
        } else if (this.checkCross(lexeme)) {
            return false;
        } else {
            this.addLexeme(lexeme);
            this.payloadLength += lexeme.getLength();
            Lexeme head = this.peekFirst();
            this.pathBegin = head.getBegin();
            Lexeme tail = this.peekLast();
            this.pathEnd = tail.getBegin() + tail.getLength();
            return true;
        }
    }

    /**
     * 移除尾部的Lexeme
     *
     * @return Lexeme
     */
    public Lexeme removeTail() {
        Lexeme tail = this.pollLast();
        if (this.isEmpty()) {
            this.pathBegin = -1;
            this.pathEnd = -1;
            this.payloadLength = 0;
        } else {
            this.payloadLength -= tail.getLength();
            Lexeme newTail = this.peekLast();
            this.pathEnd = newTail.getBegin() + newTail.getLength();
        }
        return tail;
    }

    /**
     * 检测词元位置交叉（有歧义的切分）
     *
     * @param lexeme 词元
     * @return int
     */
    public boolean checkCross(Lexeme lexeme) {
        return (lexeme.getBegin() >= this.pathBegin && lexeme.getBegin() < this.pathEnd)
                || (this.pathBegin >= lexeme.getBegin() && this.pathBegin < lexeme.getBegin() + lexeme.getLength());
    }

    /**
     * 获取LexemePath的路径长度
     *
     * @return int
     */
    public int getPathLength() {
        return this.pathEnd - this.pathBegin;
    }


    /**
     * X权重（词元长度积）
     *
     * @return int
     */
    int getXWeight() {
        int product = 1;
        QuickSortSetLinkedCell c = this.getHead();
        while (c != null && c.getLexeme() != null) {
            product *= c.getLexeme().getLength();
            c = c.getNext();
        }
        return product;
    }

    /**
     * 词元位置权重
     *
     * @return int
     */
    int getPWeight() {
        int pWeight = 0;
        int p = 0;
        QuickSortSetLinkedCell c = this.getHead();
        while (c != null && c.getLexeme() != null) {
            p++;
            pWeight += p * c.getLexeme().getLength();
            c = c.getNext();
        }
        return pWeight;
    }

    /**
     * 复制词元
     *
     * @return LexemePath
     */
    public LexemePath copy() {
        LexemePath theCopy = new LexemePath();
        theCopy.pathBegin = this.pathBegin;
        theCopy.pathEnd = this.pathEnd;
        theCopy.payloadLength = this.payloadLength;
        QuickSortSetLinkedCell cell = this.getHead();
        while (cell != null && cell.getLexeme() != null) {
            theCopy.addLexeme(cell.getLexeme());
            cell = cell.getNext();
        }
        return theCopy;
    }

    /**
     * 比较算法
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(LexemePath o) {
        //比较有效文本长度
        if (this.payloadLength > o.payloadLength) {
            return -1;
        } else if (this.payloadLength < o.payloadLength) {
            return 1;
        } else {
            //比较词元个数，越少越好
            if (this.size() < o.size()) {
                return -1;
            } else if (this.size() > o.size()) {
                return 1;
            } else {
                //路径跨度越大越好
                if (this.getPathLength() > o.getPathLength()) {
                    return -1;
                } else if (this.getPathLength() < o.getPathLength()) {
                    return 1;
                } else {
                    //根据统计学结论，逆向切分概率高于正向切分，因此位置越靠后的优先
                    if (this.pathEnd > o.pathEnd) {
                        return -1;
                    } else if (pathEnd < o.pathEnd) {
                        return 1;
                    } else {
                        //词长越平均越好
                        if (this.getXWeight() > o.getXWeight()) {
                            return -1;
                        } else if (this.getXWeight() < o.getXWeight()) {
                            return 1;
                        } else {
                            //词元位置权重比较
                            if (this.getPWeight() > o.getPWeight()) {
                                return -1;
                            } else if (this.getPWeight() < o.getPWeight()) {
                                return 1;
                            }

                        }
                    }
                }
            }
        }
        return 0;
    }

    /**
     * 序列化
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("pathBegin  : ").append(pathBegin).append("\r\n");
        sb.append("pathEnd  : ").append(pathEnd).append("\r\n");
        sb.append("payloadLength  : ").append(payloadLength).append("\r\n");
        QuickSortSetLinkedCell head = this.getHead();
        while (head != null) {
            sb.append("lexeme : ").append(head.getLexeme()).append("\r\n");
            head = head.getNext();
        }
        return sb.toString();
    }
}
