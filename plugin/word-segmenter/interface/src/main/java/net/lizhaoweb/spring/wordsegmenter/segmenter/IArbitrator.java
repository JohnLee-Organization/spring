/**
 * Copyright (c) 2016, Stupid Bird and/or its affiliates. All rights reserved.
 * STUPID BIRD PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @Project : spring
 * @Package : net.lizhaoweb.spring.wordsegmenter.segmenter
 * @author <a href="http://www.lizhaoweb.net">李召(John.Lee)</a>
 * @email 404644381@qq.com
 * @Time : 15:02
 */
package net.lizhaoweb.spring.wordsegmenter.segmenter;

/**
 * <h1>接口 - 分词歧义裁决器</h1>
 *
 * @author <a href="http://www.lizhaoweb.cn">李召(John.Lee)</a>
 * @version 1.0.0.0.1
 * @notes Created on 2016年07月30日<br>
 * Revision of last commit:$Revision$<br>
 * Author of last commit:$Author$<br>
 * Date of last commit:$Date$<br>
 */
public interface IArbitrator {

    /**
     * 分词歧义处理
     *
     * @param context  上下文件
     * @param useSmart 是否使用智能
     */
    void process(IAnalyzeContext context, boolean useSmart);
}
