/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.model
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @EMAIL 404644381@qq.com
 * @Time : 12:34
 */
package net.lizhaoweb.spring.wordsegmenter.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PACKAGE;

/**
 * <h1>模型 - QuickSortSet链表单元</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 *
 */
@ToString
@EqualsAndHashCode
@Component
public class QuickSortSetLinkedCell implements Comparable<QuickSortSetLinkedCell> {

    @Setter(value = PACKAGE)
    @Getter
    private QuickSortSetLinkedCell prev;

    @Setter(value = PACKAGE)
    @Getter
    private QuickSortSetLinkedCell next;

    @Setter
    @Getter
    @Autowired
    private Lexeme lexeme;

    @Override
    public int compareTo(QuickSortSetLinkedCell o) {
        if (this.lexeme == null) {
            throw new IllegalArgumentException("lexeme must not be null");
        }
        return this.lexeme.compareTo(o.lexeme);
    }
}
