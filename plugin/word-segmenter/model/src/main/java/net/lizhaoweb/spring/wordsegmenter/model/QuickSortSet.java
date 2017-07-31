/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 12:33
 */
package net.lizhaoweb.spring.wordsegmenter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

/**
 * <h1>模型 - 分词器专用的Lexem快速排序集合</h1>
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
public class QuickSortSet {

    /**
     * Lexeme 链表头
     */
    @Getter(value = PUBLIC)
    private QuickSortSetLinkedCell head;

    /**
     * Lexeme 链表尾
     */
    @Getter(value = PACKAGE)
    private QuickSortSetLinkedCell tail;

    /**
     * 链表的实际大小
     */
    @Getter(value = PACKAGE)
    private int size;

    public QuickSortSet() {
        this.size = 0;
    }

    /**
     * 向链表集合添加词元
     *
     * @param lexeme
     */
    public boolean addLexeme(Lexeme lexeme) {
        QuickSortSetLinkedCell newCell = new QuickSortSetLinkedCell();
        newCell.setLexeme(lexeme);
        if (this.size == 0) {
            this.head = newCell;
            this.tail = newCell;
            this.size++;
            return true;
        } else {
            if (this.tail.compareTo(newCell) == 0) {//词元与尾部词元相同，不放入集合
                return false;
            } else if (this.tail.compareTo(newCell) < 0) {//词元接入链表尾部
                this.tail.setNext(newCell);
                newCell.setPrev(this.tail);
                this.tail = newCell;
                this.size++;
                return true;
            } else if (this.head.compareTo(newCell) > 0) {//词元接入链表头部
                this.head.setPrev(newCell);
                newCell.setNext(this.head);
                this.head = newCell;
                this.size++;
                return true;
            } else {
                //从尾部上逆
                QuickSortSetLinkedCell index = this.tail;
                while (index != null && index.compareTo(newCell) > 0) {
                    index = index.getPrev();
                }
                if (index.compareTo(newCell) == 0) {//词元与集合中的词元重复，不放入集合
                    return false;
                } else if (index.compareTo(newCell) < 0) {//词元插入链表中的某个位置
                    newCell.setPrev(index);
                    newCell.setNext(index.getNext());
                    index.getNext().setPrev(newCell);
                    index.setNext(newCell);
                    this.size++;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回链表头部元素
     *
     * @return
     */
    Lexeme peekFirst() {
        if (this.head != null) {
            return this.head.getLexeme();
        }
        return null;
    }

    /**
     * 取出链表集合的第一个元素
     *
     * @return Lexeme
     */
    public Lexeme pollFirst() {
        if (this.size == 1) {
            Lexeme first = this.head.getLexeme();
            this.head = null;
            this.tail = null;
            this.size--;
            return first;
        } else if (this.size > 1) {
            Lexeme first = this.head.getLexeme();
            this.head = this.head.getNext();
            this.size--;
            return first;
        } else {
            return null;
        }
    }

    /**
     * 返回链表尾部元素
     *
     * @return
     */
    public Lexeme peekLast() {
        if (this.tail != null) {
            return this.tail.getLexeme();
        }
        return null;
    }

    /**
     * 取出链表集合的最后一个元素
     *
     * @return Lexeme
     */
    Lexeme pollLast() {
        if (this.size == 1) {
            Lexeme last = this.head.getLexeme();
            this.head = null;
            this.tail = null;
            this.size--;
            return last;
        } else if (this.size > 1) {
            Lexeme last = this.tail.getLexeme();
            this.tail = this.tail.getPrev();
            this.size--;
            return last;
        } else {
            return null;
        }
    }

    /**
     * 返回集合大小
     *
     * @return
     */
    public int size() {
        return this.size;
    }

    /**
     * 判断集合是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.size == 0;
    }
}
