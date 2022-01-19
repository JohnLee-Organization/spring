/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 12:10
 */
package net.lizhaoweb.spring.wordsegmenter.model;

import lombok.Getter;

/**
 * <h1>模型 - 词元类型</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
public enum LexemeType {

    /**
     * 未知
     */
    UNKNOWN("0", "UNKONW"),

    /**
     * 英文
     */
    ENGLISH("1", "ENGLISH"),
    /**
     * 数字
     */
    ARABIC("2", "ARABIC"),

    /**
     * 英文数字混合
     */
    LETTER("3", "LETTER"),

    /**
     * 中文词元
     */
    CNWORD("4", "CN_WORD"),

    /**
     * 中文单字
     */
    CNCHAR("64", "CN_CHAR"),

    /**
     * 日韩文字
     */
    OTHER_CJK("8", "OTHER_CJK"),

    /**
     * 中文数词
     */
    CNUM("16", "TYPE_CNUM"),

    /**
     * 中文量词
     */
    COUNT("32", "COUNT"),

    /**
     * 中文数量词
     */
    CQUAN("48", "TYPE_CQUAN");

    @Getter
    private String value;

    @Getter
    private String name;

    private LexemeType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static LexemeType fromValue(String value) {
        try {
            return valueOf(value);
        } catch (Exception e) {
            return UNKNOWN;
        }
    }
}
