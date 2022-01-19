/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.dictionary
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 14:35
 */
package net.lizhaoweb.spring.wordsegmenter.dictionary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * <h1>模型 - 一次词典匹配的命中</h1>
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
public class Hit {
    //Hit不匹配
    private static final int UNMATCH = 0x00000000;
    //Hit完全匹配
    private static final int MATCH = 0x00000001;
    //Hit前缀匹配
    private static final int PREFIX = 0x00000010;


    //该HIT当前状态，默认未匹配
    private int hitState = UNMATCH;

    /**
     * 记录词典匹配过程中，当前匹配到的词典分支节点
     */
    @Setter
    @Getter
    private DictSegment matchedDictSegment;

    /**
     * 词段开始位置
     */
    @Setter
    @Getter
    private int begin;

    /**
     * 词段的结束位置
     */
    @Setter
    @Getter
    private int end;


    /**
     * 判断是否完全匹配
     */
    public boolean isMatch() {
        return (this.hitState & MATCH) > 0;
    }

    /**
     * 设置匹配
     */
    public void setMatch() {
        this.hitState = this.hitState | MATCH;
    }

    /**
     * 设置词的前缀
     */
    public void setPrefix() {
        this.hitState = this.hitState | PREFIX;
    }

    /**
     * 判断是否是词的前缀
     */
    public boolean isPrefix() {
        return (this.hitState & PREFIX) > 0;
    }

    /**
     * 设置为不匹配
     */
    public void setUnmatch() {
        this.hitState = UNMATCH;
    }

    /**
     * 判断是否是不匹配
     */
    public boolean isUnmatch() {
        return this.hitState == UNMATCH;
    }
}
